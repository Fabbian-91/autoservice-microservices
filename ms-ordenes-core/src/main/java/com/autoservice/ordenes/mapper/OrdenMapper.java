package com.autoservice.ordenes.mapper;

import com.autoservice.ordenes.dto.OrdenRequestDTO;
import com.autoservice.ordenes.dto.OrdenResponseDTO;
import com.autoservice.ordenes.model.Orden;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface OrdenMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "diagnostico", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaIngreso", ignore = true)
    @Mapping(target = "fechaEntrega", ignore = true)
    Orden toEntity(OrdenRequestDTO dto);

    @Mapping(target = "citaId", source = "citaId")
    OrdenResponseDTO toResponse(Orden orden);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "diagnostico", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "fechaIngreso", ignore = true)
    @Mapping(target = "fechaEntrega", ignore = true)
    void updateEntity(OrdenRequestDTO dto, @MappingTarget Orden orden);
}