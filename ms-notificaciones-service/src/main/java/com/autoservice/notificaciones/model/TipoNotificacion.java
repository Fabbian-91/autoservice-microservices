package com.autoservice.notificaciones.model;

// Los 3 tipos de aviso que define el enunciado. Cada uno corresponde a una cola distinta de RabbitMQ.
public enum TipoNotificacion {
    CITA_REGISTRADA,
    VEHICULO_LISTO,
    FACTURA_GENERADA
}
