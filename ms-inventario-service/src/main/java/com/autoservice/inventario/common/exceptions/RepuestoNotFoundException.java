package com.autoservice.inventario.common.exceptions;

public class RepuestoNotFoundException extends RuntimeException {

    public RepuestoNotFoundException(Long id) {
        super("No se encontró el repuesto con id: " + id);
    }

}
