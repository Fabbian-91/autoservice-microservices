package com.autoservice.inventario.validation;

import com.autoservice.inventario.common.exceptions.CantidadFueraDeRangoException;
import com.autoservice.inventario.common.exceptions.CantidadInvalidaException;
import com.autoservice.inventario.common.exceptions.StockInsuficienteException;
import com.autoservice.inventario.model.Repuesto;
import org.springframework.stereotype.Component;

@Component
public class InventarioValidator {

    public void validarCantidad(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new CantidadInvalidaException();
        }
    }

    public int calcularStockDespuesDeEntrada(
            Repuesto repuesto,
            Integer cantidad
    ) {
        validarCantidad(cantidad);

        try {
            return Math.addExact(
                    repuesto.getCantidadDisponible(),
                    cantidad
            );
        } catch (ArithmeticException ex) {
            throw new CantidadFueraDeRangoException();
        }
    }

    public void validarStockSuficiente(
            Repuesto repuesto,
            Integer cantidad
    ) {
        validarCantidad(cantidad);

        if (cantidad > repuesto.getCantidadDisponible()) {
            throw new StockInsuficienteException(
                    repuesto.getId(),
                    cantidad,
                    repuesto.getCantidadDisponible()
            );
        }
    }

}
