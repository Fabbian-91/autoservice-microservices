package com.taller.ms_historial_service.model;

import com.taller.ms_historial_service.enums.TipoEvento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "evento_historial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_evento", nullable = false, length = 30)
    private TipoEvento tipoEvento;

    @Column(name = "entidad_id", nullable = false)
    private Long entidadId;

    @Column(length = 200)
    private String descripcion;

    @Lob
    @Column(name = "payload_json")
    private String payloadJson;

    @Column(nullable = false)
    private LocalDateTime fecha;
}