package com.taller.ms_clientes_vehiculos.common.exception;

public class VehiculoNotFoundException extends RuntimeException {

    public VehiculoNotFoundException(Long id) {
        super("Vehiculo con id: " + id + " no existe.");
    }


}
