package com.autoservice.ordenes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenRequestDTO {

    private Long citaId;

    @NotNull(message = "El clienteId es obligatorio")
    @Positive(message = "El clienteId debe ser positivo")
    private Long clienteId;

    @NotNull(message = "El vehiculoId es obligatorio")
    @Positive(message = "El vehiculoId debe ser positivo")
    private Long vehiculoId;

    @NotBlank(message = "El motivo de ingreso es obligatorio")
    @Size(min = 5, max = 200, message = "El motivo debe tener entre 5 y 200 caracteres")
    private String motivoIngreso;

    @Min(value = 0, message = "El kilometraje no puede ser negativo")
    private Integer kilometraje;
}
