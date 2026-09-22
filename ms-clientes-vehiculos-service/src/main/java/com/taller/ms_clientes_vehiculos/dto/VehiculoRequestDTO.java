package com.taller.ms_clientes_vehiculos.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiculoRequestDTO {

    @NotNull(message = "El campo 'ID de Cliente' no puede quedar vacío.")
    @Positive(message = "El ID de cliente debe ser un número positivo.")
    private Long clienteId;

    @NotBlank(message = "El campo 'PLACA' no puede quedar vacío.")
    @Size(min = 4, max = 15, message = "La placa debe estar entre 4 y 15 caracteres.")
    private String placa;

    @NotBlank(message = "El campo 'MARCA' no puede quedar vacío.")
    @Size(min = 4, max = 50, message = "La marca debe estar entre 4 y 50 caracteres.")
    private String marca;

    @NotBlank(message = "El campo 'MODELO' no puede quedar vacío.")
    @Size(min = 4, max = 50, message = "El modelo debe de estar entre los 4 y 50 caracteres.")
    private String modelo;

    @Size(min = 3, max = 30, message = "El color debe de estar entre los 3 y 30 caracteres.")
    private String color;

    @NotNull(message = "El campo 'Año' no puede quedar vacío.")
    @Max(value = 9999, message = "El año debe tener máximo 4 caracteres.")
    @Min(value = 1000, message = "El año debe tener mínimo 4 caracteres.")
    @Positive(message = "El año debe ser un numero positivo.")
    private Integer year;

}
