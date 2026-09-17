package com.autoservice.ordenes.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "respuesto_orden")
public class RepuestoOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    @Column(name = "repuesto_id", nullable = false)
    private Long repuestoId;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario",
    nullable = false,
    precision = 10,
    scale = 2
    )
    private BigDecimal precioUnitario;

    public RepuestoOrden() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public Long getRepuestoId() {
        return repuestoId;
    }

    public void setRepuestoId(Long repuestoId) {
        this.repuestoId = repuestoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
