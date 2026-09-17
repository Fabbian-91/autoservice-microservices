package com.taller.ms_historial_service.common.exception;

public class EventoKafkaException extends RuntimeException {
    public EventoKafkaException(String message) {
        super(message);
    }
    public EventoKafkaException(String message, Throwable cause) {
        super(message, cause);
    }
}
