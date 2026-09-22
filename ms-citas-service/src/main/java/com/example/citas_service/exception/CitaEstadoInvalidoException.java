package com.example.citas_service.exception;

public class CitaEstadoInvalidoException extends RuntimeException {

    public CitaEstadoInvalidoException(Long id, String estadoActual, String accion) {
        super("No se puede realizar '" + accion + "' sobre la cita " + id + " con estado " + estadoActual);
    }
}
