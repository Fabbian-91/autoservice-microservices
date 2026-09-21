package com.autoservice.facturacion.common.exception;

public class FacturaNoEncontradaException extends RuntimeException {

    public FacturaNoEncontradaException(Long id) {
        super("No existe una factura con id " + id);
    }
}
