package com.taller.ms_historial_service.dto;

import com.taller.ms_historial_service.enums.TipoEvento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoKafkaDTO {

    @NotNull(message = "El tipo de evento es obligatorio")
    private TipoEvento tipoEvento;

    @NotNull(message = "El id de la entidad es obligatorio")
    private Long entidadId;

    @Size(
            max = 200,
            message = "La descripción no puede superar los 200 caracteres"
    )
    private String descripcion;

    @NotNull(message = "La fecha del evento es obligatoria")
    private LocalDateTime fecha;
}
