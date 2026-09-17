package com.autoservice.ordenes.exception;

public class TransicionEstadoInvalidaException extends RuntimeException {

    public TransicionEstadoInvalidaException(String estadoActual, String estadoNuevo) {
        super("No se puede pasar la orden del estado '" + estadoActual + "' al estado '" + estadoNuevo + "'");
    }
}