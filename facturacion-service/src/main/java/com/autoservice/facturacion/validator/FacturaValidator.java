package com.autoservice.facturacion.validator;

import com.autoservice.facturacion.common.exception.*;
import com.autoservice.facturacion.dto.DetalleFactura.OrdenResponseDTO;
import com.autoservice.facturacion.dto.DetalleFactura.RepuestoOrdenDTO;
import com.autoservice.facturacion.dto.DetalleFactura.TrabajoFacturaRequestDTO;
import com.autoservice.facturacion.dto.DetalleFactura.TrabajoOrdenDTO;
import com.autoservice.facturacion.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FacturaValidator {

    private static final String ESTADO_FINALIZADA = "FINALIZADA";
    private static final String ESTADO_TRABAJO_TERMINADO = "TERMINADO";

    private final FacturaRepository facturaRepository;

    public void validarPuedeFacturar(OrdenResponseDTO orden) {

        if (!ESTADO_FINALIZADA.equalsIgnoreCase(orden.getEstado())) {
            throw new OrdenNoFinalizadaException(orden.getId());
        }

        if (facturaRepository.existsByOrdenId(orden.getId())) {
            throw new OrdenYaFacturadaException(orden.getId());
        }
    }

    public void validarTrabajos(List<TrabajoOrdenDTO> trabajos, Long ordenId) {

        if (trabajos == null) {
            throw new RuntimeException(
                    "No se pudieron obtener los trabajos de la orden: " + ordenId
            );
        }

        boolean hayTrabajosPendientes = trabajos.stream()
                .anyMatch(trabajo ->
                        trabajo.getEstado() == null ||
                                !ESTADO_TRABAJO_TERMINADO.equalsIgnoreCase(trabajo.getEstado())
                );

        if (hayTrabajosPendientes) {
            throw new TrabajosPendientesException(ordenId);
        }
    }

    public void validarRepuestos(
            List<RepuestoOrdenDTO> repuestos,
            Long ordenId
    ) {

        if (repuestos == null) {
            throw new RepuestosOrdenException(
                    "No se pudieron obtener los repuestos de la orden con id: " + ordenId
            );
        }

        boolean hayRepuestoInvalido = repuestos.stream()
                .anyMatch(repuesto ->
                        repuesto.getCantidad() == null ||
                                repuesto.getCantidad() <= 0 ||
                                repuesto.getPrecioUnitario() == null ||
                                repuesto.getPrecioUnitario().signum() < 0
                );

        if (hayRepuestoInvalido) {
            throw new RepuestosOrdenException(
                    "La orden con id " + ordenId +
                            " contiene repuestos con datos inválidos"
            );
        }
    }

    public void validarPreciosTrabajos(
            List<TrabajoOrdenDTO> trabajosOrden,
            List<TrabajoFacturaRequestDTO> trabajosRequest
    ) {

        // Si la orden no tiene trabajos, no hay nada que cobrar
        if (trabajosOrden.isEmpty()) {
            return;
        }

        if (trabajosRequest == null || trabajosRequest.isEmpty()) {
            throw new PrecioTrabajoException(
                    "Debe indicar el precio de todos los trabajos de la orden"
            );
        }

        // Validar ids duplicados en el request
        Set<Long> ids = new HashSet<>();

        for (TrabajoFacturaRequestDTO trabajoRequest : trabajosRequest) {

            if (!ids.add(trabajoRequest.getTrabajoId())) {
                throw new PrecioTrabajoException(
                        "El trabajo con id " +
                                trabajoRequest.getTrabajoId() +
                                " está repetido"
                );
            }
        }

        // Cada trabajo real de la orden debe tener precio
        for (TrabajoOrdenDTO trabajoOrden : trabajosOrden) {

            boolean tienePrecio = trabajosRequest.stream()
                    .anyMatch(request ->
                            request.getTrabajoId()
                                    .equals(trabajoOrden.getId())
                    );

            if (!tienePrecio) {
                throw new PrecioTrabajoException(
                        "No se indicó precio para el trabajo con id: "
                                + trabajoOrden.getId()
                );
            }
        }

        // No permitir cobrar trabajos que no pertenecen a la orden
        for (TrabajoFacturaRequestDTO trabajoRequest : trabajosRequest) {

            boolean perteneceOrden = trabajosOrden.stream()
                    .anyMatch(trabajo ->
                            trabajo.getId()
                                    .equals(trabajoRequest.getTrabajoId())
                    );

            if (!perteneceOrden) {
                throw new PrecioTrabajoException(
                        "El trabajo con id "
                                + trabajoRequest.getTrabajoId()
                                + " no pertenece a la orden"
                );
            }
        }
    }
}