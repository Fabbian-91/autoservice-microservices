package com.taller.ms_clientes_vehiculos.service;

import com.taller.ms_clientes_vehiculos.common.exception.ClienteNotFoundException;
import com.taller.ms_clientes_vehiculos.common.exception.VehiculoNotFoundException;
import com.taller.ms_clientes_vehiculos.dto.VehiculoRequestDTO;
import com.taller.ms_clientes_vehiculos.dto.VehiculoResponseDTO;
import com.taller.ms_clientes_vehiculos.mapper.VehiculoMapper;
import com.taller.ms_clientes_vehiculos.model.Cliente;
import com.taller.ms_clientes_vehiculos.model.Vehiculo;
import com.taller.ms_clientes_vehiculos.repository.ClienteRepository;
import com.taller.ms_clientes_vehiculos.repository.VehiculoRepository;
import com.taller.ms_clientes_vehiculos.validator.VehiculoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service//para que spring lo reconozca como un servicio
@RequiredArgsConstructor//para inyecciones sin constructor(x medio de spring)
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoMapper vehiculoMapper;
    private final VehiculoValidator vehiculoValidator;


    public List<VehiculoResponseDTO> getAllVehiculos(){
        List<Vehiculo> vehiculos = vehiculoRepository.findAll();

        return vehiculoMapper.toResponseDTOList(vehiculos);
    }


    public VehiculoResponseDTO getVehiculoById(Long id){
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new VehiculoNotFoundException(id));

        return vehiculoMapper.toResponseDTO(vehiculo);
    }


    public VehiculoResponseDTO createVehiculo(VehiculoRequestDTO vehiculoDTO){

        // TODO validar que el usuario exista PENDIENTE PARA CUANDO YA EXISTA EL REPO DE clientes
        Cliente cliente = clienteRepository.findById(vehiculoDTO.getClienteId())
                .orElseThrow(() -> new ClienteNotFoundException(vehiculoDTO.getClienteId()));

        //validar que la placa mo exista en DB
        vehiculoValidator.checkPlacaExists(vehiculoDTO.getPlaca());

        //paso de DTO a entidad
        Vehiculo vehiculo = vehiculoMapper.toEntity(vehiculoDTO);

        //asigno el cliente a la entidad vehiculo
        vehiculo.setCliente(cliente);

        //guardar en la base de datos
        Vehiculo vehiculoGuardado = vehiculoRepository.save(vehiculo);

        //paso de entidad a DTO de respuesta
        return vehiculoMapper.toResponseDTO(vehiculoGuardado);
    }


    public VehiculoResponseDTO updateVehiculo(Long id, VehiculoRequestDTO vehiculoDTO) {

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new VehiculoNotFoundException(id));

        //validar que la placa no exista en DB y no sea la misma que la del vehículo que se está actualizando
        vehiculoValidator.checkPlacaExistsForUpdate(vehiculoDTO.getPlaca(), id);

        //validar que el cliente exista xq el cliente puede cambiarse en la actualización del vehículo
        Cliente cliente = clienteRepository.findById(vehiculoDTO.getClienteId())
                .orElseThrow(() -> new ClienteNotFoundException(vehiculoDTO.getClienteId()));

        //asigno el cliente a la entidad vehiculo
        vehiculo.setCliente(cliente);

        //paso de DTO a entidad, actualizando solo lo que venga en el dto.
         vehiculoMapper.updateEntity(vehiculo, vehiculoDTO);

        //guardar en la base de datos
        vehiculo = vehiculoRepository.save(vehiculo);

        //paso de entidad a DTO de respuesta
        return vehiculoMapper.toResponseDTO(vehiculo);

    }


    public  VehiculoResponseDTO deleteVehiculo(Long id) {

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new VehiculoNotFoundException(id));

        vehiculoRepository.delete(vehiculo);

        return vehiculoMapper.toResponseDTO(vehiculo);
    }
}
