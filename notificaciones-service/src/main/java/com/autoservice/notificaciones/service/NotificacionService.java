package com.autoservice.notificaciones.service;

import com.autoservice.notificaciones.dto.CitaRegistradaEventoDTO;
import com.autoservice.notificaciones.dto.FacturaGeneradaEventoDTO;
import com.autoservice.notificaciones.dto.NotificacionResponseDTO;
import com.autoservice.notificaciones.dto.VehiculoListoEventoDTO;
import com.autoservice.notificaciones.exception.NotificacionNoEncontradaException;
import com.autoservice.notificaciones.mapper.NotificacionMapper;
import com.autoservice.notificaciones.model.EstadoNotificacion;
import com.autoservice.notificaciones.model.Notificacion;
import com.autoservice.notificaciones.model.TipoNotificacion;
import com.autoservice.notificaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacionService {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final NotificacionRepository notificacionRepository;
    private final NotificacionMapper notificacionMapper;

    public void procesarCitaRegistrada(CitaRegistradaEventoDTO evento) {
        String mensaje = "Su cita para el %s a las %s fue registrada correctamente."
                .formatted(evento.getFecha().format(FORMATO_FECHA), evento.getHora().format(FORMATO_HORA));
        registrarYEnviar(TipoNotificacion.CITA_REGISTRADA, evento.getClienteId(), mensaje);
    }

    public void procesarVehiculoListo(VehiculoListoEventoDTO evento) {
        String mensaje = "Su vehiculo con placa %s ya esta listo para retirar.".formatted(evento.getPlaca());
        registrarYEnviar(TipoNotificacion.VEHICULO_LISTO, evento.getClienteId(), mensaje);
    }

    public void procesarFacturaGenerada(FacturaGeneradaEventoDTO evento) {
        String mensaje = "Se genero su factura por un total de %.2f.".formatted(evento.getTotal());
        registrarYEnviar(TipoNotificacion.FACTURA_GENERADA, evento.getClienteId(), mensaje);
    }

    private void registrarYEnviar(TipoNotificacion tipo, Long clienteId, String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setTipo(tipo);
        notificacion.setDestinatarioId(clienteId);
        notificacion.setMensaje(mensaje);
        notificacion.setEstado(EstadoNotificacion.PENDIENTE);
        notificacion.setFechaCreacion(LocalDateTime.now());
        notificacionRepository.save(notificacion);

        // Aca en un caso real se llamaria a un proveedor de email/SMS. El enunciado pide solo simularlo.
        log.info("Simulando envio al cliente {} -> {}", clienteId, mensaje);

        notificacion.setEstado(EstadoNotificacion.ENVIADA);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }

    public NotificacionResponseDTO buscarPorId(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new NotificacionNoEncontradaException(id));
        return notificacionMapper.toResponse(notificacion);
    }

    public List<NotificacionResponseDTO> listarTodas() {
        return notificacionMapper.toResponseList(notificacionRepository.findAll());
    }

    public List<NotificacionResponseDTO> listarPorCliente(Long clienteId) {
        return notificacionMapper.toResponseList(notificacionRepository.findByDestinatarioId(clienteId));
    }
}
