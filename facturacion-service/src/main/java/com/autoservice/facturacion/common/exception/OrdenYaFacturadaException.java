package com.autoservice.facturacion.common.exception;

public class OrdenYaFacturadaException extends RuntimeException {

    public OrdenYaFacturadaException(Long ordenId) {
        super("La orden " + ordenId + " ya tiene una factura generada");
    }
}
