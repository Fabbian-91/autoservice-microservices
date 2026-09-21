package com.autoservice.facturacion.model;

import com.autoservice.facturacion.enums.EstadoFactura;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long ordenId;

    @NotNull
    @Column(nullable = false)
    private Long clienteId;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime fechaEmision;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal subtotal;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal impuesto;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal total;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoFactura estado;

    @OneToMany(
            mappedBy = "factura",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<DetalleFactura> detalles = new ArrayList<>();
}