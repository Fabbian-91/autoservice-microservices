package com.autoservice.inventario.validation;

import com.autoservice.inventario.common.exceptions.RepuestoConMovimientosException;
import com.autoservice.inventario.repository.MovimientoInventarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RepuestoValidator {

    private final MovimientoInventarioRepository movimientoRepository;

    public void validarQueSePuedeEliminar(Long repuestoId) {
        if (movimientoRepository.existsByRepuestoId(repuestoId)) {
            throw new RepuestoConMovimientosException(repuestoId);
        }
    }

}
