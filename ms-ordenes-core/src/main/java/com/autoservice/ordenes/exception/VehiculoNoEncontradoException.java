package com.autoservice.ordenes.exception;

public class VehiculoNoEncontradoException extends RuntimeException {

    public VehiculoNoEncontradoException(Long id) {
        super("El vehículo con id: " + id + " no existe en el servicio de Clientes/Vehículos");
    }
}