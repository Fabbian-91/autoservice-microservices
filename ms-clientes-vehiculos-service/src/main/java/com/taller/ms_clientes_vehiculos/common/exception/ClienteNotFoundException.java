package com.taller.ms_clientes_vehiculos.common.exception;

public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(Long id) {
        super("Cliente no encontrado con id: " + id);
    }

    public ClienteNotFoundException(String mensaje) {
        super(mensaje);
    }
}