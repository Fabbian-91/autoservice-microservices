package com.autoservice.inventario.mapper;

import com.autoservice.inventario.dto.MovimientoInventarioResponseDTO;
import com.autoservice.inventario.model.MovimientoInventario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MovimientoInventarioMapper {

    @Mappings({
            @Mapping(target = "repuestoId", source = "repuesto.id"),
            @Mapping(target = "repuestoNombre", source = "repuesto.nombre")
    })
    MovimientoInventarioResponseDTO toResponse(MovimientoInventario movimiento);

}
