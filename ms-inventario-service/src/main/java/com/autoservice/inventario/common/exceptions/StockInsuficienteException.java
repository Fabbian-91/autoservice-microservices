package com.autoservice.inventario.common.exceptions;

public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(Long repuestoId, int solicitado, int disponible) {
        super("Stock insuficiente para el repuesto " + repuestoId
                + ". Solicitado: " + solicitado
                + ", disponible: " + disponible);
    }

}
