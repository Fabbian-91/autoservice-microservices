package com.autoservice.facturacion.mapper;

import com.autoservice.facturacion.dto.FacturaResponseDTO;
import com.autoservice.facturacion.model.Factura;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacturaMapper {

    FacturaResponseDTO toResponse(Factura factura);

    List<FacturaResponseDTO> toResponseList(List<Factura> facturas);
}
