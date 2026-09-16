package com.autoservice.ordenes.service;

import com.autoservice.ordenes.client.ClienteClient;
import com.autoservice.ordenes.client.VehiculoClient;
import com.autoservice.ordenes.enums.EstadoOrden;
import com.autoservice.ordenes.dto.ClienteDTO;
import com.autoservice.ordenes.dto.DiagnosticoDTO;
import com.autoservice.ordenes.dto.OrdenRequestDTO;
import com.autoservice.ordenes.dto.OrdenResponseDTO;
import com.autoservice.ordenes.dto.VehiculoDTO;
import com.autoservice.ordenes.exception.ClienteNoEncontradoException;
import com.autoservice.ordenes.exception.DiagnosticoRequeridoException;
import com.autoservice.ordenes.exception.EstadoOrdenInvalidoException;
import com.autoservice.ordenes.exception.OrdenNotFoundException;
import com.autoservice.ordenes.exception.OrdenYaEntregadaException;
import com.autoservice.ordenes.exception.VehiculoNoEncontradoException;
import com.autoservice.ordenes.mapper.OrdenMapper;
import com.autoservice.ordenes.model.Orden;
import com.autoservice.ordenes.repository.OrdenRepository;
import com.autoservice.ordenes.validator.OrdenValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final OrdenMapper ordenMapper;
    private final OrdenValidator ordenValidator;
    private final VehiculoClient vehiculoClient;
    private final ClienteClient clienteClient;

    public OrdenService(OrdenRepository ordenRepository, OrdenMapper ordenMapper, OrdenValidator ordenValidator,
                        VehiculoClient vehiculoClient, ClienteClient clienteClient) {
        this.ordenRepository = ordenRepository;
        this.ordenMapper = ordenMapper;
        this.ordenValidator = ordenValidator;
        this.vehiculoClient = vehiculoClient;
        this.clienteClient = clienteClient;
    }

    public List<OrdenResponseDTO> listarTodas() {
        return ordenRepository.findAll().stream()
                .map(this::detallesAdicionales)
                .collect(Collectors.toList());
    }

    public List<OrdenResponseDTO> listarPorCliente(Long clienteId) {
        return ordenRepository.findByClienteId(clienteId).stream()
                .map(this::detallesAdicionales)
                .collect(Collectors.toList());
    }

    public List<OrdenResponseDTO> listarPorVehiculo(Long vehiculoId) {
        return ordenRepository.findByVehiculoId(vehiculoId).stream()
                .map(this::detallesAdicionales)
                .collect(Collectors.toList());
    }

    public OrdenResponseDTO obtenerPorId(Long id) {
        Orden orden = buscarOrden(id);
        return detallesAdicionales(orden);
    }

    public OrdenResponseDTO crearOrden(OrdenRequestDTO dto) {
        validarVehiculo(dto.getVehiculoId());
        validarCliente(dto.getClienteId());

        Orden orden = ordenMapper.toEntity(dto);
        orden.setEstado(EstadoOrden.INGRESADA);
        orden.setFechaIngreso(LocalDateTime.now());

        Orden guardada = ordenRepository.save(orden);
        return detallesAdicionales(guardada);
    }

    public OrdenResponseDTO registrarDiagnostico(Long id, DiagnosticoDTO dto) {
        Orden orden = buscarOrden(id);
        ordenValidator.checkOrdenNoEntregada(orden.getEstado().name(), id);
        ordenValidator.checkTransicionValida(orden.getEstado().name(), EstadoOrden.DIAGNOSTICO.name());

        orden.setDiagnostico(dto.getDiagnostico());
        orden.setEstado(EstadoOrden.DIAGNOSTICO);
        Orden guardada = ordenRepository.save(orden);
        return detallesAdicionales(guardada);
    }

    public OrdenResponseDTO cambiarEstado(Long id, String nuevoEstado) {
        Orden orden = buscarOrden(id);
        ordenValidator.checkOrdenNoEntregada(orden.getEstado().name(), id);

        EstadoOrden destino;
        try {
            destino = EstadoOrden.valueOf(nuevoEstado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new EstadoOrdenInvalidoException(nuevoEstado);
        }

        ordenValidator.checkTransicionValida(orden.getEstado().name(), destino.name());

        if (destino == EstadoOrden.FINALIZADA && (orden.getDiagnostico() == null || orden.getDiagnostico().isBlank())) {
            throw new DiagnosticoRequeridoException(id);
        }

        orden.setEstado(destino);

        if (destino == EstadoOrden.ENTREGADA) {
            orden.setFechaEntrega(LocalDateTime.now());
        }

        Orden guardada = ordenRepository.save(orden);
        return detallesAdicionales(guardada);
    }

    public void eliminarOrden(Long id) {
        Orden orden = buscarOrden(id);
        ordenValidator.checkOrdenNoEntregada(orden.getEstado().name(), id);
        ordenRepository.delete(orden);
    }

    public OrdenResponseDTO actualizarOrden(Long id, OrdenRequestDTO dto) {
        Orden orden = buscarOrden(id);
        ordenValidator.checkOrdenNoEntregada(orden.getEstado().name(), id);

        if (dto.getVehiculoId() != null) {
            validarVehiculo(dto.getVehiculoId());
        }
        if (dto.getClienteId() != null) {
            validarCliente(dto.getClienteId());
        }

        ordenMapper.updateEntity(dto, orden);
        Orden guardada = ordenRepository.save(orden);
        return detallesAdicionales(guardada);
    }

    // ==================== Métodos privados ====================

    private Orden buscarOrden(Long id) {
        return ordenRepository.findById(id)
                .orElseThrow(() -> new OrdenNotFoundException(id));
    }

    private void validarVehiculo(Long vehiculoId) {
        try {
            VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(vehiculoId);
            if (vehiculo == null) {
                throw new VehiculoNoEncontradoException(vehiculoId);
            }
        } catch (VehiculoNoEncontradoException ex) {
            throw ex;
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new VehiculoNoEncontradoException(vehiculoId);
            }
            log.error("Error consultando vehículo {}: {}", vehiculoId, ex.getMessage());
            throw new RuntimeException("El servicio de Vehículos no está disponible", ex);
        } catch (ResourceAccessException ex) {
            log.error("Servicio de Vehículos no disponible: {}", ex.getMessage());
            throw ex;
        }
    }

    private void validarCliente(Long clienteId) {
        try {
            ClienteDTO cliente = clienteClient.obtenerCliente(clienteId);
            if (cliente == null) {
                throw new ClienteNoEncontradoException(clienteId);
            }
        } catch (ClienteNoEncontradoException ex) {
            throw ex;
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new ClienteNoEncontradoException(clienteId);
            }
            log.error("Error consultando cliente {}: {}", clienteId, ex.getMessage());
            throw new RuntimeException("El servicio de Clientes no está disponible", ex);
        } catch (ResourceAccessException ex) {
            log.error("Servicio de Clientes no disponible: {}", ex.getMessage());
            throw ex;
        }
    }

    private OrdenResponseDTO detallesAdicionales(Orden orden) {
        OrdenResponseDTO response = ordenMapper.toResponse(orden);
        try {
            VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(orden.getVehiculoId());
            response.setVehiculo(vehiculo);
        } catch (Exception e) {
            log.warn("No se pudo obtener el vehículo {} de la orden {}: {}", orden.getVehiculoId(), orden.getId(), e.getMessage());
        }
        return response;
    }
}