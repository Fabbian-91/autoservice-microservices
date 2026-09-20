package com.autoservice.ordenes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActualizarTrabajoRequest(

        @NotBlank(message = "La descripción es obligatoria")
        @Size(
                max = 200,
                message = "La descripción no puede superar 200 caracteres"
        )
        String descripcion

) {
}