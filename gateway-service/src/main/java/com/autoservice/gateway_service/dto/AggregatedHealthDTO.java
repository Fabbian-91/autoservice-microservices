package com.autoservice.gateway_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

//Respuesta agregada del estado de todos los microservicios
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedHealthDTO {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private String estadoGeneral;    // UP, DEGRADED, DOWN

    private List<ServiceHealthDTO> servicios;

    private Map<String, Integer> resumen;  // total, arriba, abajo
}