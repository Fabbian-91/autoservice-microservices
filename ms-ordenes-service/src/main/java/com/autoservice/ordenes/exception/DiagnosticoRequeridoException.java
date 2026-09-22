package com.autoservice.ordenes.exception;

public class DiagnosticoRequeridoException extends RuntimeException {

    public DiagnosticoRequeridoException(Long id) {
        super("La orden con id: " + id + " requiere registrar el diagnóstico antes de continuar");
    }
}