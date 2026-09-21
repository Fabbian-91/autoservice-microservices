package com.autoservice.notificaciones.dto;

import com.autoservice.notificaciones.model.EstadoNotificacion;
import com.autoservice.notificaciones.model.TipoNotificacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionResponseDTO {

    private Long id;
    private TipoNotificacion tipo;
    private Long destinatarioId;
    private String mensaje;
    private EstadoNotificacion estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEnvio;
}
