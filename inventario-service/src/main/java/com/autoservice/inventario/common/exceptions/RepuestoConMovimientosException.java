package com.autoservice.inventario.common.exceptions;

public class RepuestoConMovimientosException extends RuntimeException {

    public RepuestoConMovimientosException(Long id) {
        super("No se puede eliminar el repuesto " + id
                + " porque tiene movimientos de inventario registrados");
    }

}
