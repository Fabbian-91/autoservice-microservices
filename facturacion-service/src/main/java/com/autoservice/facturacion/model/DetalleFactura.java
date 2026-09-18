package com.autoservice.facturacion.model;

import com.autoservice.facturacion.enums.TipoDetalle;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "tbDetalleFactura")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "factura_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Factura factura;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoDetalle tipo;

    @NotBlank
    @Size(max = 200)
    private String descripcion;

    @NotNull
    @Positive
    private Integer cantidad;

    @NotNull
    private BigDecimal precioUnitario;

    @NotNull
    private BigDecimal subtotal;
}
