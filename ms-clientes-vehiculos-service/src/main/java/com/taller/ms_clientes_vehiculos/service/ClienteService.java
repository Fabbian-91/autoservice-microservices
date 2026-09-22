package com.taller.ms_clientes_vehiculos.service;

import com.taller.ms_clientes_vehiculos.common.exception.ClienteNotFoundException;
import com.taller.ms_clientes_vehiculos.dto.ClienteRequestDTO;
import com.taller.ms_clientes_vehiculos.dto.ClienteResponseDTO;
import com.taller.ms_clientes_vehiculos.mapper.ClienteMapper;
import com.taller.ms_clientes_vehiculos.model.Cliente;
import com.taller.ms_clientes_vehiculos.repository.ClienteRepository;
import com.taller.ms_clientes_vehiculos.validator.ClienteValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final ClienteValidator clienteValidator;

    public List<ClienteResponseDTO> listarTodos() {
        List<Cliente> lista = clienteRepository.findAll();
        return lista.stream().map(clienteMapper::toResponseDTO).toList();
    }

    public ClienteResponseDTO obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));

        //mapeo
        return clienteMapper.toResponseDTO(cliente);
    }

    public ClienteResponseDTO buscarPorCedula(String cedula) {
        Cliente cliente = clienteRepository.findByCedula(cedula)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con cédula: " + cedula));
        return clienteMapper.toResponseDTO(cliente);
    }

    public List<ClienteResponseDTO> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(clienteMapper::toResponseDTO)
                .toList();
    }

    public ClienteResponseDTO crear(ClienteRequestDTO dto) {

        //reglas de negocio
        clienteValidator.validarCedulaUnica(dto.cedula());

        //mapear de DTO a entity para enviar a guardar
        Cliente cliente = clienteMapper.toEntity(dto);

        cliente = clienteRepository.save(cliente);

        //despues de guardar mapear de entity a responseDTO
        return clienteMapper.toResponseDTO(cliente);
    }

    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        //validar que exista
        var clienteActualBD = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));

        if (!clienteActualBD.getCedula().equals(dto.cedula())) {
            clienteValidator.validarCedulaUnica(dto.cedula());
        }

        //mapeo
        clienteMapper.updateEntity(clienteActualBD, dto);

        clienteActualBD = clienteRepository.save(clienteActualBD);

        //mapeo response dto
        return clienteMapper.toResponseDTO(clienteActualBD);
    }

    public ClienteResponseDTO eliminar(Long id) {
        //validar que exista
        var clienteActualBD = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException(id));

        clienteRepository.delete(clienteActualBD);

        //mapeo a response dto
        return clienteMapper.toResponseDTO(clienteActualBD);
    }
}