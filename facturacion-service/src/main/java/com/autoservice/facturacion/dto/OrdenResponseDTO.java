package com.autoservice.facturacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenResponseDTO {

    private Long id;
    private Long clienteId;
    private String estado;
}
