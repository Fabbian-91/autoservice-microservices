package com.example.citas_service.service;

import com.example.citas_service.client.ClienteClient;
import com.example.citas_service.client.VehiculoClient;
import com.example.citas_service.dto.*;
import com.example.citas_service.event.CitaCreadaEvent;
import com.example.citas_service.exception.CitaEstadoInvalidoException;
import com.example.citas_service.exception.CitaHorarioOcupadoException;
import com.example.citas_service.exception.CitaNotFoundException;
import com.example.citas_service.exception.CitaYaCanceladaException;
import com.example.citas_service.model.Cita;
import com.example.citas_service.model.EstadoCita;
import com.example.citas_service.publisher.CitaEventPublisher;
import com.example.citas_service.publisher.CitaNotificationPublisher;
import com.example.citas_service.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final ClienteClient clienteClient;
    private final VehiculoClient vehiculoClient;

    @Autowired(required = false)
    private CitaEventPublisher citaEventPublisher;

    @Autowired(required = false)
    private CitaNotificationPublisher citaNotificationPublisher;

    public CitaResponseDTO crearCita(CitaRequestDTO request) {
        ClienteDTO cliente = clienteClient.obtenerCliente(request.getClienteId());
        VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(request.getVehiculoId());

        List<Cita> citasExistentes = citaRepository.findByFechaAndEstado(
                request.getFecha(), EstadoCita.PENDIENTE);
        citasExistentes.addAll(citaRepository.findByFechaAndEstado(
                request.getFecha(), EstadoCita.CONFIRMADA));

        boolean horarioOcupado = citasExistentes.stream()
                .anyMatch(c -> c.getHora().equals(request.getHora()));
        if (horarioOcupado) {
            throw new CitaHorarioOcupadoException(
                    request.getFecha().toString(), request.getHora().toString());
        }

        Cita cita = Cita.builder()
                .clienteId(request.getClienteId())
                .vehiculoId(request.getVehiculoId())
                .fecha(request.getFecha())
                .hora(request.getHora())
                .motivo(request.getMotivo())
                .estado(EstadoCita.PENDIENTE)
                .build();

        Cita citaGuardada = citaRepository.save(cita);
        log.info("Cita creada con id: {}", citaGuardada.getId());

        CitaCreadaEvent event = new CitaCreadaEvent(
                citaGuardada.getId(),
                citaGuardada.getClienteId(),
                citaGuardada.getVehiculoId(),
                citaGuardada.getFecha(),
                citaGuardada.getHora(),
                citaGuardada.getFechaCreacion()
        );

        if (citaEventPublisher != null) {
            try {
                citaEventPublisher.publicarCitaCreada(event);
            } catch (Exception e) {
                log.error("Error al publicar evento Kafka para cita {}: {}", citaGuardada.getId(), e.getMessage());
            }
        }

        if (citaNotificationPublisher != null) {
            try {
                citaNotificationPublisher.notificarCitaCreada(event);
            } catch (Exception e) {
                log.error("Error al enviar notificacion RabbitMQ para cita {}: {}", citaGuardada.getId(), e.getMessage());
            }
        }

        return toResponse(citaGuardada, cliente, vehiculo);
    }

    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    public CitaResponseDTO obtenerPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));

        ClienteDTO cliente = clienteClient.obtenerCliente(cita.getClienteId());
        VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(cita.getVehiculoId());

        return toResponse(cita, cliente, vehiculo);
    }

    public CitaResponseDTO cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));

        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new CitaYaCanceladaException(id);
        }

        cita.setEstado(EstadoCita.CANCELADA);
        Cita citaGuardada = citaRepository.save(cita);

        ClienteDTO cliente = clienteClient.obtenerCliente(cita.getClienteId());
        VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(cita.getVehiculoId());

        return toResponse(citaGuardada, cliente, vehiculo);
    }

    public CitaResponseDTO confirmarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));

        cita.setEstado(EstadoCita.CONFIRMADA);
        Cita citaGuardada = citaRepository.save(cita);

        ClienteDTO cliente = clienteClient.obtenerCliente(cita.getClienteId());
        VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(cita.getVehiculoId());

        return toResponse(citaGuardada, cliente, vehiculo);
    }

    public CitaResponseDTO atenderCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));

        cita.setEstado(EstadoCita.ATENDIDA);
        Cita citaGuardada = citaRepository.save(cita);

        ClienteDTO cliente = clienteClient.obtenerCliente(cita.getClienteId());
        VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(cita.getVehiculoId());

        return toResponse(citaGuardada, cliente, vehiculo);
    }

    public CitaResponseDTO marcarNoAsistio(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));

        if (cita.getEstado() != EstadoCita.CONFIRMADA) {
            throw new CitaEstadoInvalidoException(id, cita.getEstado().name(), "no-asistio");
        }

        cita.setEstado(EstadoCita.NO_ASISTIO);
        Cita citaGuardada = citaRepository.save(cita);

        ClienteDTO cliente = clienteClient.obtenerCliente(cita.getClienteId());
        VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(cita.getVehiculoId());

        return toResponse(citaGuardada, cliente, vehiculo);
    }

    private CitaResponseDTO toResponse(Cita cita, ClienteDTO cliente, VehiculoDTO vehiculo) {
        CitaResponseDTO response = new CitaResponseDTO();
        response.setId(cita.getId());
        response.setCliente(cliente);
        response.setVehiculo(vehiculo);
        response.setFecha(cita.getFecha());
        response.setHora(cita.getHora());
        response.setMotivo(cita.getMotivo());
        response.setEstado(cita.getEstado());
        response.setFechaCreacion(cita.getFechaCreacion());
        return response;
    }
}
