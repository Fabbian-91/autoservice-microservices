package com.autoservice.ordenes.exception;

public class OrdenNotFoundException extends RuntimeException {

    public OrdenNotFoundException(Long id) {
        super("Orden no encontrada con id: " + id);
    }
}