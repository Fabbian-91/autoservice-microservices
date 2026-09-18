package com.autoservice.ordenes.exception;

public class OrdenYaEntregadaException extends RuntimeException {

    public OrdenYaEntregadaException(Long id) {
        super("La orden con id: " + id + " ya fue entregada y no puede ser editada");
    }
}