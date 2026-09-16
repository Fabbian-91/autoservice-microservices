package com.example.citas_service.exception;

public class CitaYaCanceladaException extends RuntimeException {

    public CitaYaCanceladaException(Long id) {
        super("La cita con id " + id + " ya está cancelada");
    }
}
