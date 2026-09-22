package com.autoservice.inventario.service;

import com.autoservice.inventario.common.enums.TipoMovimiento;
import com.autoservice.inventario.common.exceptions.RepuestoNotFoundException;
import com.autoservice.inventario.dto.DisponibilidadResponseDTO;
import com.autoservice.inventario.dto.EntradaInventarioRequestDTO;
import com.autoservice.inventario.dto.MovimientoInventarioResponseDTO;
import com.autoservice.inventario.dto.SalidaInventarioRequestDTO;
import com.autoservice.inventario.mapper.MovimientoInventarioMapper;
import com.autoservice.inventario.model.MovimientoInventario;
import com.autoservice.inventario.model.Repuesto;
import com.autoservice.inventario.repository.MovimientoInventarioRepository;
import com.autoservice.inventario.repository.RepuestoRepository;
import com.autoservice.inventario.validation.InventarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventarioService {

    private final RepuestoRepository repuestoRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final MovimientoInventarioMapper movimientoMapper;
    private final InventarioValidator inventarioValidator;

    @Transactional
    public MovimientoInventarioResponseDTO registrarEntrada(
            Long repuestoId,
            EntradaInventarioRequestDTO request
    ) {
        Repuesto repuesto = buscarConBloqueo(repuestoId);

        repuesto.setCantidadDisponible(
                inventarioValidator.calcularStockDespuesDeEntrada(
                        repuesto,
                        request.getCantidad()
                )
        );

        MovimientoInventario movimiento = crearMovimiento(
                repuesto,
                TipoMovimiento.ENTRADA,
                request.getCantidad(),
                null
        );

        repuestoRepository.save(repuesto);
        return movimientoMapper.toResponse(movimientoRepository.save(movimiento));
    }

    @Transactional
    public MovimientoInventarioResponseDTO registrarSalida(
            Long repuestoId,
            SalidaInventarioRequestDTO request
    ) {
        Repuesto repuesto = buscarConBloqueo(repuestoId);

        inventarioValidator.validarStockSuficiente(
                repuesto,
                request.getCantidad()
        );

        repuesto.setCantidadDisponible(
                repuesto.getCantidadDisponible() - request.getCantidad()
        );

        MovimientoInventario movimiento = crearMovimiento(
                repuesto,
                TipoMovimiento.SALIDA,
                request.getCantidad(),
                request.getOrdenId()
        );

        repuestoRepository.save(repuesto);
        return movimientoMapper.toResponse(movimientoRepository.save(movimiento));
    }

    public DisponibilidadResponseDTO consultarDisponibilidad(
            Long repuestoId,
            Integer cantidad
    ) {
        inventarioValidator.validarCantidad(cantidad);

        Repuesto repuesto = repuestoRepository.findById(repuestoId)
                .orElseThrow(() -> new RepuestoNotFoundException(repuestoId));

        return new DisponibilidadResponseDTO(
                repuestoId,
                cantidad,
                repuesto.getCantidadDisponible(),
                repuesto.getCantidadDisponible() >= cantidad
        );
    }

    public List<MovimientoInventarioResponseDTO> listarMovimientos() {
        return movimientoRepository.findAllByOrderByFechaDesc()
                .stream()
                .map(movimientoMapper::toResponse)
                .toList();
    }

    public List<MovimientoInventarioResponseDTO> listarPorRepuesto(Long repuestoId) {
        if (!repuestoRepository.existsById(repuestoId)) {
            throw new RepuestoNotFoundException(repuestoId);
        }

        return movimientoRepository.findByRepuestoIdOrderByFechaDesc(repuestoId)
                .stream()
                .map(movimientoMapper::toResponse)
                .toList();
    }

    public List<MovimientoInventarioResponseDTO> listarPorOrden(Long ordenId) {
        return movimientoRepository.findByOrdenIdOrderByFechaDesc(ordenId)
                .stream()
                .map(movimientoMapper::toResponse)
                .toList();
    }

    private Repuesto buscarConBloqueo(Long id) {
        return repuestoRepository.findByIdConBloqueo(id)
                .orElseThrow(() -> new RepuestoNotFoundException(id));
    }

    private MovimientoInventario crearMovimiento(
            Repuesto repuesto,
            TipoMovimiento tipo,
            Integer cantidad,
            Long ordenId
    ) {
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setRepuesto(repuesto);
        movimiento.setTipo(tipo);
        movimiento.setCantidad(cantidad);
        movimiento.setOrdenId(ordenId);
        return movimiento;
    }

}
