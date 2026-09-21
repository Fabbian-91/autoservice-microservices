package com.autoservice.ordenes.dto;

public record UsuarioResponse(
        Long id,
        String nombre,
        String correo,
        String rol,
        Boolean activo
) {
}