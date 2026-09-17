package com.autoservice.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DisponibilidadResponseDTO {

    private Long repuestoId;
    private Integer cantidadSolicitada;
    private Integer cantidadDisponible;
    private boolean disponible;

}
