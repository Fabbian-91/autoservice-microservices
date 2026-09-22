package com.taller.ms_clientes_vehiculos.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "La cédula es obligatoria") String cedula,
        String telefono,
        String correo,
        String direccion
) {}
