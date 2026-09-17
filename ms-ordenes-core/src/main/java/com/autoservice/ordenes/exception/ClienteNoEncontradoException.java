package com.autoservice.ordenes.exception;

public class ClienteNoEncontradoException extends RuntimeException {

    public ClienteNoEncontradoException(Long id) {
        super("El cliente con id: " + id + " no existe en el servicio de Clientes/Vehículos");
    }
}