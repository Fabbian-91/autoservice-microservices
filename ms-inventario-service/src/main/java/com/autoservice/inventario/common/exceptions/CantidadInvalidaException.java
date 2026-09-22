package com.autoservice.inventario.common.exceptions;

public class CantidadInvalidaException extends RuntimeException {

    public CantidadInvalidaException() {
        super("La cantidad debe ser mayor que cero");
    }

}
