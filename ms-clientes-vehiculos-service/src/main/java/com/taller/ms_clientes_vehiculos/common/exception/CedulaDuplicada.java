package com.taller.ms_clientes_vehiculos.common.exception;

public class CedulaDuplicada extends RuntimeException {
    public CedulaDuplicada(String cedula) {
        super("Ya existe un cliente registrado con la cédula: " + cedula);
    }
}