package com.example.citas_service.service;

import com.example.citas_service.client.ClienteClient;
import com.example.citas_service.client.VehiculoClient;
import com.example.citas_service.dto.*;
import com.example.citas_service.exception.CitaEstadoInvalidoException;
import com.example.citas_service.exception.CitaHorarioOcupadoException;
import com.example.citas_service.exception.CitaNotFoundException;
import com.example.citas_service.model.Cita;
import com.example.citas_service.model.EstadoCita;
import com.example.citas_service.publisher.CitaEventPublisher;
import com.example.citas_service.publisher.CitaNotificationPublisher;
import com.example.citas_service.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final ClienteClient clienteClient;
    private final VehiculoClient vehiculoClient;
    private final CitaEventPublisher citaEventPublisher;
    private final CitaNotificationPublisher citaNotificationPublisher;

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

        Cita cita = new Cita();
        cita.setClienteId(request.getClienteId());
        cita.setVehiculoId(request.getVehiculoId());
        cita.setFecha(request.getFecha());
        cita.setHora(request.getHora());
        cita.setMotivo(request.getMotivo());
        cita.setEstado(EstadoCita.PENDIENTE);

        Cita citaGuardada = citaRepository.save(cita);
        log.info("Cita creada con id: {}", citaGuardada.getId());

        publicarEventoSiEsNecesario("CITA_CREADA", citaGuardada.getId(),
                "Cita registrada para el " + citaGuardada.getFecha());

        try {
            citaNotificationPublisher.notificarCitaCreada(
                    citaGuardada.getClienteId(),
                    citaGuardada.getFecha(),
                    citaGuardada.getHora()
            );
        } catch (Exception e) {
            log.error("Error al enviar notificacion RabbitMQ para cita {}: {}", citaGuardada.getId(), e.getMessage());
        }

        return toResponse(citaGuardada, cliente, vehiculo);
    }

    public List<CitaResponseDTO> listarTodas() {
        List<Cita> citas = citaRepository.findAll();
        return citas.stream()
                .map(cita -> {
                    ClienteDTO cliente = clienteClient.obtenerCliente(cita.getClienteId());
                    VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(cita.getVehiculoId());
                    return toResponse(cita, cliente, vehiculo);
                })
                .toList();
    }

    public List<CitaResponseDTO> buscarPorCliente(Long clienteId) {
        List<Cita> citas = citaRepository.findByClienteId(clienteId);
        return citas.stream()
                .map(cita -> {
                    ClienteDTO cliente = clienteClient.obtenerCliente(cita.getClienteId());
                    VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(cita.getVehiculoId());
                    return toResponse(cita, cliente, vehiculo);
                })
                .toList();
    }

    public List<CitaResponseDTO> buscarPorEstado(EstadoCita estado) {
        List<Cita> citas = citaRepository.findByEstado(estado);
        return citas.stream()
                .map(cita -> {
                    ClienteDTO cliente = clienteClient.obtenerCliente(cita.getClienteId());
                    VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(cita.getVehiculoId());
                    return toResponse(cita, cliente, vehiculo);
                })
                .toList();
    }

    public List<CitaResponseDTO> buscarPorFecha(LocalDate fecha) {
        List<Cita> citas = citaRepository.findByFechaBetween(fecha, fecha);
        return citas.stream()
                .map(cita -> {
                    ClienteDTO cliente = clienteClient.obtenerCliente(cita.getClienteId());
                    VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(cita.getVehiculoId());
                    return toResponse(cita, cliente, vehiculo);
                })
                .toList();
    }

    public CitaResponseDTO obtenerPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));

        ClienteDTO cliente = clienteClient.obtenerCliente(cita.getClienteId());
        VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(cita.getVehiculoId());

        return toResponse(cita, cliente, vehiculo);
    }

    public CitaResponseDTO confirmarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));

        if (cita.getEstado() != EstadoCita.PENDIENTE) {
            throw new CitaEstadoInvalidoException(id, cita.getEstado().name(), "confirmar");
        }

        cita.setEstado(EstadoCita.CONFIRMADA);
        Cita citaGuardada = citaRepository.save(cita);
        log.info("Cita {} confirmada", id);

        publicarEventoSiEsNecesario("CITA_CONFIRMADA", citaGuardada.getId(),
                "Cita confirmada para el " + citaGuardada.getFecha());

        ClienteDTO cliente = clienteClient.obtenerCliente(cita.getClienteId());
        VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(cita.getVehiculoId());

        return toResponse(citaGuardada, cliente, vehiculo);
    }

    public CitaResponseDTO cancelarCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));

        if (cita.getEstado() != EstadoCita.PENDIENTE && cita.getEstado() != EstadoCita.CONFIRMADA) {
            throw new CitaEstadoInvalidoException(id, cita.getEstado().name(), "cancelar");
        }

        cita.setEstado(EstadoCita.CANCELADA);
        Cita citaGuardada = citaRepository.save(cita);
        log.info("Cita {} cancelada", id);

        publicarEventoSiEsNecesario("CITA_CANCELADA", citaGuardada.getId(),
                "Cita cancelada que estaba en estado " + cita.getEstado().name());

        ClienteDTO cliente = clienteClient.obtenerCliente(cita.getClienteId());
        VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(cita.getVehiculoId());

        return toResponse(citaGuardada, cliente, vehiculo);
    }

    public CitaResponseDTO atenderCita(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));

        if (cita.getEstado() != EstadoCita.CONFIRMADA) {
            throw new CitaEstadoInvalidoException(id, cita.getEstado().name(), "atender");
        }

        cita.setEstado(EstadoCita.ATENDIDA);
        Cita citaGuardada = citaRepository.save(cita);
        log.info("Cita {} atendida", id);

        publicarEventoSiEsNecesario("CITA_ATENDIDA", citaGuardada.getId(),
                "Cita atendida el " + citaGuardada.getFecha());

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
        log.info("Cita {} marcada como no asistio", id);

        publicarEventoSiEsNecesario("CITA_NO_ASISTIO", citaGuardada.getId(),
                "Cliente no asistio a la cita del " + citaGuardada.getFecha());

        ClienteDTO cliente = clienteClient.obtenerCliente(cita.getClienteId());
        VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(cita.getVehiculoId());

        return toResponse(citaGuardada, cliente, vehiculo);
    }

    public CitaResponseDTO editarCita(Long id, CitaUpdateDTO request) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new CitaNotFoundException(id));

        if (cita.getEstado() == EstadoCita.CANCELADA
                || cita.getEstado() == EstadoCita.ATENDIDA
                || cita.getEstado() == EstadoCita.NO_ASISTIO) {
            throw new CitaEstadoInvalidoException(id, cita.getEstado().name(), "editar");
        }

        if (request.getFecha() != null) {
            cita.setFecha(request.getFecha());
        }
        if (request.getHora() != null) {
            cita.setHora(request.getHora());
        }
        if (request.getMotivo() != null) {
            cita.setMotivo(request.getMotivo());
        }

        LocalDate fechaFinal = cita.getFecha();
        LocalTime horaFinal = cita.getHora();

        List<Cita> citasExistentes = citaRepository.findByFechaAndEstado(fechaFinal, EstadoCita.PENDIENTE);
        citasExistentes.addAll(citaRepository.findByFechaAndEstado(fechaFinal, EstadoCita.CONFIRMADA));

        boolean horarioOcupado = citasExistentes.stream()
                .filter(c -> !c.getId().equals(id))
                .anyMatch(c -> c.getHora().equals(horaFinal));
        if (horarioOcupado) {
            throw new CitaHorarioOcupadoException(fechaFinal.toString(), horaFinal.toString());
        }

        Cita citaGuardada = citaRepository.save(cita);
        log.info("Cita {} actualizada", id);

        ClienteDTO cliente = clienteClient.obtenerCliente(citaGuardada.getClienteId());
        VehiculoDTO vehiculo = vehiculoClient.obtenerVehiculo(citaGuardada.getVehiculoId());

        return toResponse(citaGuardada, cliente, vehiculo);
    }

    private void publicarEventoSiEsNecesario(String tipoEvento, Long citaId, String descripcion) {
        try {
            citaEventPublisher.publicarEvento(tipoEvento, citaId, descripcion);
        } catch (Exception e) {
            log.error("Error al publicar evento {} Kafka para cita {}: {}", tipoEvento, citaId, e.getMessage());
        }
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
