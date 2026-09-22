package com.example.citas_service;

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
import com.example.citas_service.service.CitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private VehiculoClient vehiculoClient;

    @Mock
    private CitaEventPublisher citaEventPublisher;

    @Mock
    private CitaNotificationPublisher citaNotificationPublisher;

    @InjectMocks
    private CitaService citaService;

    private ClienteDTO clienteDTO;
    private VehiculoDTO vehiculoDTO;

    @BeforeEach
    void setUp() {
        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre("Juan Perez");
        clienteDTO.setTelefono("88888888");
        clienteDTO.setCorreo("juan@test.com");

        vehiculoDTO = new VehiculoDTO();
        vehiculoDTO.setId(1L);
        vehiculoDTO.setPlaca("ABC123");
        vehiculoDTO.setMarca("Toyota");
        vehiculoDTO.setModelo("Corolla");
    }

    private Cita crearCitaMock(Long id, EstadoCita estado) {
        Cita cita = new Cita();
        cita.setId(id);
        cita.setClienteId(1L);
        cita.setVehiculoId(1L);
        cita.setFecha(LocalDate.now().plusDays(1));
        cita.setHora(LocalTime.of(9, 0));
        cita.setMotivo("Cambio de aceite");
        cita.setEstado(estado);
        cita.setFechaCreacion(LocalDateTime.now());
        return cita;
    }

    // --- Crear cita ---

    @Test
    void crearCita_deberiaCrearExitosamente() {
        CitaRequestDTO request = new CitaRequestDTO();
        request.setClienteId(1L);
        request.setVehiculoId(1L);
        request.setFecha(LocalDate.now().plusDays(1));
        request.setHora(LocalTime.of(9, 0));
        request.setMotivo("Cambio de aceite");

        when(clienteClient.obtenerCliente(1L)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(vehiculoDTO);
        when(citaRepository.findByFechaAndEstado(any(), any())).thenReturn(new ArrayList<>());
        when(citaRepository.save(any())).thenAnswer(invocation -> {
            Cita c = invocation.getArgument(0);
            c.setId(1L);
            c.setFechaCreacion(LocalDateTime.now());
            return c;
        });

        CitaResponseDTO response = citaService.crearCita(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(EstadoCita.PENDIENTE, response.getEstado());
        verify(citaEventPublisher).publicarEvento(eq("CITA_CREADA"), eq(1L), anyString());
        verify(citaNotificationPublisher).notificarCitaCreada(eq(1L), any(), any());
    }

    @Test
    void crearCita_horarioOcupado_deberiaLanzarExcepcion() {
        CitaRequestDTO request = new CitaRequestDTO();
        request.setClienteId(1L);
        request.setVehiculoId(1L);
        request.setFecha(LocalDate.now().plusDays(1));
        request.setHora(LocalTime.of(9, 0));

        Cita existente = crearCitaMock(2L, EstadoCita.PENDIENTE);

        when(clienteClient.obtenerCliente(1L)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(vehiculoDTO);
        when(citaRepository.findByFechaAndEstado(any(), eq(EstadoCita.PENDIENTE)))
                .thenReturn(new ArrayList<>(List.of(existente)));
        when(citaRepository.findByFechaAndEstado(any(), eq(EstadoCita.CONFIRMADA)))
                .thenReturn(new ArrayList<>());

        assertThrows(CitaHorarioOcupadoException.class, () -> citaService.crearCita(request));
    }

    // --- Confirmar cita ---

    @Test
    void confirmarCita_desdePendiente_deberiaConfirmar() {
        Cita cita = crearCitaMock(1L, EstadoCita.PENDIENTE);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(clienteClient.obtenerCliente(1L)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(vehiculoDTO);

        CitaResponseDTO response = citaService.confirmarCita(1L);

        assertEquals(EstadoCita.CONFIRMADA, response.getEstado());
    }

    @Test
    void confirmarCita_desdeConfirmada_deberiaLanzarExcepcion() {
        Cita cita = crearCitaMock(1L, EstadoCita.CONFIRMADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        assertThrows(CitaEstadoInvalidoException.class, () -> citaService.confirmarCita(1L));
    }

    @Test
    void confirmarCita_desdeCancelada_deberiaLanzarExcepcion() {
        Cita cita = crearCitaMock(1L, EstadoCita.CANCELADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        assertThrows(CitaEstadoInvalidoException.class, () -> citaService.confirmarCita(1L));
    }

    // --- Cancelar cita ---

    @Test
    void cancelarCita_desdePendiente_deberiaCancelar() {
        Cita cita = crearCitaMock(1L, EstadoCita.PENDIENTE);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(clienteClient.obtenerCliente(1L)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(vehiculoDTO);

        CitaResponseDTO response = citaService.cancelarCita(1L);

        assertEquals(EstadoCita.CANCELADA, response.getEstado());
    }

    @Test
    void cancelarCita_desdeConfirmada_deberiaCancelar() {
        Cita cita = crearCitaMock(1L, EstadoCita.CONFIRMADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(clienteClient.obtenerCliente(1L)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(vehiculoDTO);

        CitaResponseDTO response = citaService.cancelarCita(1L);

        assertEquals(EstadoCita.CANCELADA, response.getEstado());
    }

    @Test
    void cancelarCita_desdeAtendida_deberiaLanzarExcepcion() {
        Cita cita = crearCitaMock(1L, EstadoCita.ATENDIDA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        assertThrows(CitaEstadoInvalidoException.class, () -> citaService.cancelarCita(1L));
    }

    // --- Atender cita ---

    @Test
    void atenderCita_desdeConfirmada_deberiaAtender() {
        Cita cita = crearCitaMock(1L, EstadoCita.CONFIRMADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(clienteClient.obtenerCliente(1L)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(vehiculoDTO);

        CitaResponseDTO response = citaService.atenderCita(1L);

        assertEquals(EstadoCita.ATENDIDA, response.getEstado());
    }

    @Test
    void atenderCita_desdePendiente_deberiaLanzarExcepcion() {
        Cita cita = crearCitaMock(1L, EstadoCita.PENDIENTE);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        assertThrows(CitaEstadoInvalidoException.class, () -> citaService.atenderCita(1L));
    }

    // --- No asistio ---

    @Test
    void marcarNoAsistio_desdeConfirmada_deberiaFuncionar() {
        Cita cita = crearCitaMock(1L, EstadoCita.CONFIRMADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(clienteClient.obtenerCliente(1L)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(vehiculoDTO);

        CitaResponseDTO response = citaService.marcarNoAsistio(1L);

        assertEquals(EstadoCita.NO_ASISTIO, response.getEstado());
    }

    @Test
    void marcarNoAsistio_desdePendiente_deberiaLanzarExcepcion() {
        Cita cita = crearCitaMock(1L, EstadoCita.PENDIENTE);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        assertThrows(CitaEstadoInvalidoException.class, () -> citaService.marcarNoAsistio(1L));
    }

    // --- Obtener por id ---

    @Test
    void obtenerPorId_citaNoExiste_deberiaLanzarExcepcion() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CitaNotFoundException.class, () -> citaService.obtenerPorId(99L));
    }

    @Test
    void obtenerPorId_citaExiste_deberiaRetornarDTO() {
        Cita cita = crearCitaMock(1L, EstadoCita.PENDIENTE);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(clienteClient.obtenerCliente(1L)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(vehiculoDTO);

        CitaResponseDTO response = citaService.obtenerPorId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    // --- Editar cita ---

    @Test
    void editarCita_cambiarFechaYHora_deberiaActualizar() {
        Cita cita = crearCitaMock(1L, EstadoCita.PENDIENTE);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.findByFechaAndEstado(any(), any())).thenReturn(new ArrayList<>());
        when(citaRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(clienteClient.obtenerCliente(1L)).thenReturn(clienteDTO);
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(vehiculoDTO);

        CitaUpdateDTO update = new CitaUpdateDTO();
        update.setFecha(LocalDate.now().plusDays(5));
        update.setHora(LocalTime.of(14, 0));

        CitaResponseDTO response = citaService.editarCita(1L, update);

        assertEquals(LocalDate.now().plusDays(5), response.getFecha());
        assertEquals(LocalTime.of(14, 0), response.getHora());
    }

    @Test
    void editarCita_citaCancelada_deberiaLanzarExcepcion() {
        Cita cita = crearCitaMock(1L, EstadoCita.CANCELADA);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        CitaUpdateDTO update = new CitaUpdateDTO();
        update.setMotivo("Nuevo motivo");

        assertThrows(CitaEstadoInvalidoException.class, () -> citaService.editarCita(1L, update));
    }
}
