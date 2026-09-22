package com.taller.ms_clientes_vehiculos.mapper;

import com.taller.ms_clientes_vehiculos.dto.ClienteRequestDTO;
import com.taller.ms_clientes_vehiculos.dto.ClienteResponseDTO;
import com.taller.ms_clientes_vehiculos.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.nombre());
        cliente.setCedula(dto.cedula());
        cliente.setTelefono(dto.telefono());
        cliente.setCorreo(dto.correo());
        cliente.setDireccion(dto.direccion());
        return cliente;
    }

    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getCedula(),
                cliente.getTelefono(),
                cliente.getCorreo(),
                cliente.getDireccion(),
                cliente.getFechaCreacion()
        );
    }

    //actualiza la entidad existente con los datos del DTO
    public void updateEntity(Cliente clienteActual, ClienteRequestDTO dto) {
        clienteActual.setNombre(dto.nombre());
        clienteActual.setCedula(dto.cedula());
        clienteActual.setTelefono(dto.telefono());
        clienteActual.setCorreo(dto.correo());
        clienteActual.setDireccion(dto.direccion());
    }
}
