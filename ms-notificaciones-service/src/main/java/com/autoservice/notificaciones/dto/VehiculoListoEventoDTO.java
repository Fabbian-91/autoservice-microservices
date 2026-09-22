package com.autoservice.notificaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Lo que el servicio de Ordenes va a publicar cuando una reparacion termina.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoListoEventoDTO {

    @NotNull
    private Long clienteId;

    @NotBlank
    private String placa;
}
