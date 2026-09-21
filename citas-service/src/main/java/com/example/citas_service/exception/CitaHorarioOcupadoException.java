package com.example.citas_service.exception;

public class CitaHorarioOcupadoException extends RuntimeException {

    public CitaHorarioOcupadoException(String fecha, String hora) {
        super("Ya existe una cita programada para el " + fecha + " a las " + hora);
    }
}
