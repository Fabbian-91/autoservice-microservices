package com.autoservice.facturacion.common.exception;

public class OrdenNoFinalizadaException extends RuntimeException {
    public OrdenNoFinalizadaException(Long ordenId) {
        super("La orden " + ordenId + " todavia no esta finalizada, no se puede facturar");
    }
}
