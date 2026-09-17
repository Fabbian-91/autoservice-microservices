package com.autoservice.usuarios.common.exceptions;

public class RolInvalidoException extends RuntimeException {

    public RolInvalidoException(String rol) {
        super("Rol no permitido: " + rol);
    }
}
