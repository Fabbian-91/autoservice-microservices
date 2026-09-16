package com.taller.ms_clientes_vehiculos.mapper;


import com.taller.ms_clientes_vehiculos.dto.VehiculoRequestDTO;
import com.taller.ms_clientes_vehiculos.dto.VehiculoResponseDTO;
import com.taller.ms_clientes_vehiculos.model.Vehiculo;
import org.mapstruct.*;
import java.util.List;


@Mapper(componentModel = "spring")//para que spring lo reconozca como un bean y lo inyecte en el servicio
public interface VehiculoMapper {

    @Mapping(target = "id", ignore = true)
    Vehiculo toEntity(VehiculoRequestDTO dto);

    VehiculoResponseDTO toResponseDTO(Vehiculo vehiculo);


    //le digo que si el DTO tiene un valor nulo, no se debe actualizar el valor correspondiente en la entidad
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(
            //que mapee el DTO a la entidad existente, y no cree una nueva
            @MappingTarget Vehiculo vehiculo,
            VehiculoRequestDTO dto
    );

    // para mapear una lista de entidades a una lista de DTO
    List<VehiculoResponseDTO> toResponseDTOList(List<Vehiculo> vehiculos);


}
