package com.autoservice.facturacion.common.exception;

public class EventoKafkaException extends RuntimeException {
    public EventoKafkaException(
            String mensaje,
            Throwable causa
    ) {
        super(mensaje, causa);
    }
}
