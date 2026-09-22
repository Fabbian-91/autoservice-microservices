package com.autoservice.inventario.mapper;

import com.autoservice.inventario.dto.RepuestoRequestDTO;
import com.autoservice.inventario.dto.RepuestoResponseDTO;
import com.autoservice.inventario.model.Repuesto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RepuestoMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "cantidadDisponible", ignore = true),
            @Mapping(target = "fechaActualizacion", ignore = true)
    })
    Repuesto toEntity(RepuestoRequestDTO request);

    RepuestoResponseDTO toResponse(Repuesto repuesto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "cantidadDisponible", ignore = true),
            @Mapping(target = "fechaActualizacion", ignore = true)
    })
    void actualizarEntidad(
            RepuestoRequestDTO request,
            @MappingTarget Repuesto repuesto
    );

}
