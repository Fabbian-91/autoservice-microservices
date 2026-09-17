package com.autoservice.facturacion.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbFactura")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // no son FK reales, son de otra base
    @NotNull
    private Long ordenId;

    @NotNull
    private Long clienteId;

    @NotNull
    private LocalDateTime fechaEmision;

    @NotNull
    private BigDecimal subtotal;

    @NotNull
    private BigDecimal impuesto;

    @NotNull
    private BigDecimal total;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleFactura> detalles = new ArrayList<>();
}
