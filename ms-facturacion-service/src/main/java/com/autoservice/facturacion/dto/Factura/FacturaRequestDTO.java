package com.autoservice.facturacion.dto.Factura;

import com.autoservice.facturacion.dto.DetalleFactura.TrabajoFacturaRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaRequestDTO {
    @NotNull(message = "El id de orden no puede ser nulo")
    private Long ordenId;

    @Valid
    private List<TrabajoFacturaRequestDTO> trabajos;
}
