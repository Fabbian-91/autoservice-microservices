package com.autoservice.ordenes.model;

import com.autoservice.ordenes.enums.EstadoOrden;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orden")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long citaId;

    @Column(nullable = false)
    private Long clienteId;

    @Column(nullable = false)
    private Long vehiculoId;

    private Long mecanicoId;

    @Column(nullable = false, length = 200)
    private String motivoIngreso;

    private Integer kilometraje;

    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoOrden estado;

    @Column(nullable = false)
    private LocalDateTime fechaIngreso;

    private LocalDateTime fechaEntrega;

    // Constructor vacío requerido por JPA
    public Orden() {
    }
}
