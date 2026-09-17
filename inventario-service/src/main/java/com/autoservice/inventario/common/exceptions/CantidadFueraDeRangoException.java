package com.autoservice.inventario.common.exceptions;

public class CantidadFueraDeRangoException extends RuntimeException {

    public CantidadFueraDeRangoException() {
        super("La operación supera la cantidad máxima permitida por el sistema");
    }

}
