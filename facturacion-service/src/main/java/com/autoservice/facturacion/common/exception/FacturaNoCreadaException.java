package com.autoservice.facturacion.common.exception;

public class FacturaNoCreadaException extends RuntimeException {

    public FacturaNoCreadaException(String message) {
        super(message);
    }

    public FacturaNoCreadaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
