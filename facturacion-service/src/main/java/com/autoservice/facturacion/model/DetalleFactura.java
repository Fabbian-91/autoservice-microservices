package com.autoservice.facturacion.model;

import com.autoservice.facturacion.enums.TipoDetalle;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tbDetalleFactura")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoDetalle tipo;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String descripcion;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal precioUnitario;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal subtotal;


}
