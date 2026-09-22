package com.autoservice.ordenes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearTrabajoRequest(

        @NotBlank(message = "La descripción del trabajo es obligatoria")
        @Size(
                max = 200,
                message = "La descripción no puede superar los 200 caracteres"
        )
        String descripcion

) {
}