package com.autoservice.facturacion.service;

import com.autoservice.facturacion.client.OrdenClient;
import com.autoservice.facturacion.common.exception.FacturaNoCreadaException;
import com.autoservice.facturacion.common.exception.FacturaNoEncontradaException;
import com.autoservice.facturacion.common.exception.OrdenNoEncontradaException;
import com.autoservice.facturacion.common.exception.PrecioTrabajoException;
import com.autoservice.facturacion.dto.DetalleFactura.OrdenResponseDTO;
import com.autoservice.facturacion.dto.DetalleFactura.RepuestoOrdenDTO;
import com.autoservice.facturacion.dto.DetalleFactura.TrabajoFacturaRequestDTO;
import com.autoservice.facturacion.dto.DetalleFactura.TrabajoOrdenDTO;
import com.autoservice.facturacion.dto.Factura.FacturaRequestDTO;
import com.autoservice.facturacion.dto.Factura.FacturaResponseDTO;
import com.autoservice.facturacion.enums.EstadoFactura;
import com.autoservice.facturacion.enums.TipoDetalle;
import com.autoservice.facturacion.kafka.FacturaEventoPublisher;
import com.autoservice.facturacion.kafka.FacturaGeneradaEvento;
import com.autoservice.facturacion.mapper.FacturaMapper;
import com.autoservice.facturacion.model.DetalleFactura;
import com.autoservice.facturacion.model.Factura;
import com.autoservice.facturacion.rabbit.FacturaNotificacionPublisher;
import com.autoservice.facturacion.rabbit.FacturaGeneradaEventoDTO;
import com.autoservice.facturacion.repository.FacturaRepository;
import com.autoservice.facturacion.validator.FacturaValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaService {
    private static final BigDecimal TASA_IMPUESTO =
            new BigDecimal("0.13");

    //inyección de dependecias
    private final FacturaRepository _facturaRepository;
    private final FacturaMapper _facturaMapper;
    private final FacturaValidator _facturaValidator;
    private final OrdenClient _ordenesClient;
    private final FacturaNotificacionPublisher facturaNotificacionPublisher;
    private final FacturaEventoPublisher facturaEventoPublisher;

    /**
     * Metodopara listar todas las facturas
     *
     * @return
     */
    public List<FacturaResponseDTO> listarFacturas() {
        List<FacturaResponseDTO> facturas = _facturaRepository.findAll()
                .stream()
                .map(_facturaMapper::toResponse)
                .toList();
        return facturas;
    }

    /**
     * Metodo para obtener la factura por id
     *
     * @param id
     * @return
     */
    public FacturaResponseDTO obtenerPorId(Long id) {
        Factura response = _facturaRepository.findById(id)
                .orElseThrow(() -> new FacturaNoEncontradaException(id));
        return _facturaMapper.toResponse(response);
    }

    /**
     * Metodo para crear factura
     * @param requestDTO
     * @return
     */
    @Transactional(rollbackOn = Exception.class)
    public FacturaResponseDTO crearFactura(FacturaRequestDTO requestDTO) {
        //Obtengo la orden
        OrdenResponseDTO responseOrden = _ordenesClient
                .obtenerOrden(requestDTO.getOrdenId());

        //Validar que la orden exista
        if (responseOrden == null) {
            throw new OrdenNoEncontradaException(
                    "No se encontró la orden con id: %d".formatted(requestDTO.getOrdenId())
            );
        }

        //Validar que la orden no este finalizada y que esa orden no se encuentre finalizada
        _facturaValidator.validarPuedeFacturar(responseOrden);

        // Obtener trabajos
        List<TrabajoOrdenDTO> trabajoOrdenDTO =
                _ordenesClient.obtenerTrabajos(responseOrden.getId());

        // Validar trabajos
        _facturaValidator.validarTrabajos(
                trabajoOrdenDTO,
                responseOrden.getId()
        );

        // Validar precios enviados para los trabajos
        _facturaValidator.validarPreciosTrabajos(
                trabajoOrdenDTO,
                requestDTO.getTrabajos()
        );

        //Obtener repuestos
        List<RepuestoOrdenDTO> repuestoOrdenDTOS =
                _ordenesClient.obtenerRepuestos(responseOrden.getId());

        // Validar repuestos
        _facturaValidator.validarRepuestos(
                repuestoOrdenDTOS,
                responseOrden.getId()
        );

        //Map para obtener el id de trabajo y su precio
        Map<Long, BigDecimal> preciosTrabajos =
                requestDTO.getTrabajos() == null
                        ? Map.of()
                        : requestDTO.getTrabajos()
                        .stream()
                        .collect(Collectors.toMap(
                                TrabajoFacturaRequestDTO::getTrabajoId,
                                TrabajoFacturaRequestDTO::getPrecioUnitario
                        ));

        List<DetalleFactura> detalles = new ArrayList<>();


        // Detalles de TRABAJOS
        for (TrabajoOrdenDTO trabajo : trabajoOrdenDTO) {

            // Buscar el precio que mandó Facturación
            BigDecimal precioUnitario =
                    preciosTrabajos.get(trabajo.getId());

            if (precioUnitario == null) {
                throw new PrecioTrabajoException(
                        "No se indicó precio para el trabajo con id: "
                                + trabajo.getId()
                );
            }

            // Cada registro representa un trabajo
            int cantidad = 1;

            // subtotal = cantidad × precio
            BigDecimal subtotalDetalle =
                    precioUnitario.multiply(
                            BigDecimal.valueOf(cantidad)
                    );

            DetalleFactura detalleTrabajo =
                    DetalleFactura.builder()
                            .tipo(TipoDetalle.TRABAJO)
                            .descripcion(trabajo.getDescripcion())
                            .cantidad(cantidad)
                            .precioUnitario(precioUnitario)
                            .subtotal(subtotalDetalle)
                            .build();

            detalles.add(detalleTrabajo);
        }

        // Detalles de REPUESTOS
        for (RepuestoOrdenDTO repuesto : repuestoOrdenDTOS) {

            BigDecimal cantidad =
                    BigDecimal.valueOf(repuesto.getCantidad());

            BigDecimal subtotalDetalle =
                    repuesto.getPrecioUnitario().multiply(cantidad);

            DetalleFactura detalleRepuesto = DetalleFactura.builder()
                    .tipo(TipoDetalle.REPUESTO)
                    .descripcion("Repuesto #" + repuesto.getRepuestoId())
                    .cantidad(repuesto.getCantidad())
                    .precioUnitario(repuesto.getPrecioUnitario())
                    .subtotal(subtotalDetalle)
                    .build();

            detalles.add(detalleRepuesto);
        }

        //Calcular Subtotal
        BigDecimal subtotal = detalles.stream()
                .map(DetalleFactura::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Calcular Impuesto
        BigDecimal impuesto = subtotal.multiply(TASA_IMPUESTO);


        //Calcular Total
        BigDecimal total = subtotal.add(impuesto);


        //Crear Factura
        Factura factura = Factura.builder()
                .ordenId(responseOrden.getId())
                .clienteId(responseOrden.getClienteId())
                .fechaEmision(LocalDateTime.now())
                .subtotal(subtotal)
                .impuesto(impuesto)
                .total(total)
                .estado(EstadoFactura.GENERADA)
                .build();


        //Asociar detalles
        detalles.forEach(
                detalle -> detalle.setFactura(factura)
        );

        //Cambiamos los detalles
        factura.setDetalles(detalles);

        //Guardamos la factura
        Factura facturaGuardada =
                _facturaRepository.save(factura);

        //Construir evento
        FacturaGeneradaEventoDTO eventoRabbit = FacturaGeneradaEventoDTO.builder()
                .clienteId(facturaGuardada.getClienteId())
                .total(facturaGuardada.getTotal())
                .build();

        //publicar evento en rabbit
        facturaNotificacionPublisher.publicarFacturaGenerada(eventoRabbit);

        //
        FacturaGeneradaEvento eventoKafka =
                FacturaGeneradaEvento.builder()
                        .tipoEvento("FACTURA_GENERADA")
                        .entidadId(facturaGuardada.getId())
                        .descripcion(
                                "Factura generada para la orden "
                                        + facturaGuardada.getOrdenId()
                        )
                        .fecha(LocalDateTime.now())
                        .build();

        facturaEventoPublisher.publicarFacturacion(eventoKafka);

        return _facturaMapper.toResponse(facturaGuardada);
    }

    @Transactional(rollbackOn = Exception.class)
    public FacturaResponseDTO actualizarFactura(FacturaRequestDTO requestDTO,Long id){

        //Traer la traza de factura actualizar
        Factura facturaActualizar=_facturaRepository.getReferenceById(id);

        //Validamos que la factura exista
        if (facturaActualizar==null){
            throw new FacturaNoEncontradaException(id);
        }

        //Validamos que la factura no esta anulada
        _facturaValidator.validarEstadoFactura(facturaActualizar);

        //Obtengo la orden asociada
        OrdenResponseDTO ordenAsociada =_ordenesClient.obtenerOrden(requestDTO.getOrdenId());

        if (ordenAsociada==null){
            throw new OrdenNoEncontradaException("La orden con el id: d% no fue encontrada".formatted(requestDTO.getOrdenId()));
        }

        //Validar que la orden si se puede modificar
        _facturaValidator.validarPuedeModificarFactura(ordenAsociada);

    }
}
