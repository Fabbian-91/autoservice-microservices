package com.autoservice.notificaciones.exception;

public class NotificacionNoEncontradaException extends RuntimeException {

    public NotificacionNoEncontradaException(Long id) {
        super("No existe una notificacion con id " + id);
    }
}
