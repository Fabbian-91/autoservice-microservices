package com.taller.ms_clientes_vehiculos.mapper;

import com.taller.ms_clientes_vehiculos.dto.VehiculoRequestDTO;
import com.taller.ms_clientes_vehiculos.dto.VehiculoResponseDTO;
import com.taller.ms_clientes_vehiculos.model.Vehiculo;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehiculoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    Vehiculo toEntity(VehiculoRequestDTO dto);

    @Mapping(source = "cliente.id", target = "clienteId")
    VehiculoResponseDTO toResponseDTO(Vehiculo vehiculo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntity(
            @MappingTarget Vehiculo vehiculo,
            VehiculoRequestDTO dto
    );

    List<VehiculoResponseDTO> toResponseDTOList(List<Vehiculo> vehiculos);
}