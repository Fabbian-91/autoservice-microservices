package com.autoservice.notificaciones.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbNotificacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoNotificacion tipo;

    // Id del cliente en clientes_vehiculos_db. No es FK real: ese servicio ni existe todavia para nosotros.
    @NotNull
    private Long destinatarioId;

    @NotBlank
    @Size(max = 300)
    private String mensaje;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoNotificacion estado;

    @NotNull
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaEnvio;
}
