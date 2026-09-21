package com.autoservice.ordenes.exception;

public class EstadoOrdenInvalidoException extends RuntimeException {

    public EstadoOrdenInvalidoException(String estado) {
        super("El estado '" + estado + "' no es válido. Estados permitidos: INGRESADA, DIAGNOSTICO, REPARACION, FINALIZADA, ENTREGADA");
    }
}