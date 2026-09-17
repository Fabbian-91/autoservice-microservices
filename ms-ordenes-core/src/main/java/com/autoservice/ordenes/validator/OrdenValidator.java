package com.autoservice.ordenes.validator;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrdenValidator {

    private static final List<EstadoTransicion> TRANSICIONES_VALIDAS = Arrays.asList(
            new EstadoTransicion("INGRESADA", "DIAGNOSTICO"),
            new EstadoTransicion("DIAGNOSTICO", "REPARACION"),
            new EstadoTransicion("REPARACION", "FINALIZADA"),
            new EstadoTransicion("FINALIZADA", "ENTREGADA"),
            new EstadoTransicion("DIAGNOSTICO", "INGRESADA"),
            new EstadoTransicion("REPARACION", "DIAGNOSTICO"),
            new EstadoTransicion("FINALIZADA", "REPARACION")
    );

    private record EstadoTransicion(String origen, String destino) {
    }

    public void checkTransicionValida(String estadoActual, String estadoNuevo) {
        boolean valida = TRANSICIONES_VALIDAS.stream()
                .anyMatch(t -> t.origen().equals(estadoActual) && t.destino().equals(estadoNuevo));
        if (!valida) {
            throw new com.autoservice.ordenes.exception.TransicionEstadoInvalidaException(estadoActual, estadoNuevo);
        }
    }

    public void checkOrdenNoEntregada(String estado, Long ordenId) {
        if ("ENTREGADA".equals(estado)) {
            throw new com.autoservice.ordenes.exception.OrdenYaEntregadaException(ordenId);
        }
    }

    }