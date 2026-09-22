package com.autoservice.facturacion.common.exception;

public class TrabajosPendientesException extends RuntimeException {
    public TrabajosPendientesException(Long ordenId) {
        super("La orden con id " + ordenId +
                " tiene trabajos que aún no están terminados");
    }
}
