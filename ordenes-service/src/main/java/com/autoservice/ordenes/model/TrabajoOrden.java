package com.autoservice.ordenes.model;

import jakarta.persistence.*;

@Entity
@Table(name = "trabajo_Orden")
public class TrabajoOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @Column(nullable = false, length = 200)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoTrabajo estado;

    public TrabajoOrden() {
        this.estado = EstadoTrabajo.PENDIENTE;
    }

    public TrabajoOrden(Orden orden, String descripcion) {
        this.orden = orden;
        this.descripcion = descripcion;
        this.estado = EstadoTrabajo.PENDIENTE;
    }

    public Long getId() {
        return id;
    }


    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoTrabajo getEstado() {
        return estado;
    }

    public void setEstado(EstadoTrabajo estado) {
        this.estado = estado;
    }
}
