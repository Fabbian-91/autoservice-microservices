package com.taller.ms_historial_service.dto;

import com.taller.ms_historial_service.enums.TipoEvento;
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
public class EventoHistorialResponseDTO {

    private Long id;

    private TipoEvento tipoEvento;

    private Long entidadId;

    private String descripcion;

    private String payloadJson;

    private LocalDateTime fecha;
}