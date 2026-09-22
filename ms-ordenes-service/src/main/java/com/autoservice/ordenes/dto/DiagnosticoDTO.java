package com.autoservice.ordenes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosticoDTO {

    @NotBlank(message = "El diagnóstico es obligatorio")
    @Size(min = 10, max = 2000, message = "El diagnóstico debe tener entre 10 y 2000 caracteres")
    private String diagnostico;
}
