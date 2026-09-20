package com.example.citas_service.exception;

public class CitaNotFoundException extends RuntimeException {

    public CitaNotFoundException(Long id) {
        super("Cita no encontrada con id: " + id);
    }
}
