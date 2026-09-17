package com.autoservice.ordenes.service;

import com.autoservice.ordenes.dto.ActualizarTrabajoRequest;
import com.autoservice.ordenes.dto.CrearTrabajoRequest;
import com.autoservice.ordenes.dto.TrabajoResponse;
import com.autoservice.ordenes.exception.RecursoNoEncontradoException;
import com.autoservice.ordenes.exception.ReglaNegocioException;
import com.autoservice.ordenes.kafka.OrdenEventoPublisher;
import com.autoservice.ordenes.model.EstadoTrabajo;
import com.autoservice.ordenes.model.Orden;
import com.autoservice.ordenes.model.TrabajoOrden;
import com.autoservice.ordenes.repository.OrdenRepository;
import com.autoservice.ordenes.repository.TrabajoOrdenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrabajoOrdenService {

    private final TrabajoOrdenRepository trabajoRepository;
    private final OrdenRepository ordenRepository;
    private final OrdenEventoPublisher eventoPublisher;


    public TrabajoOrdenService(
            TrabajoOrdenRepository trabajoRepository,
            OrdenRepository ordenRepository,
            OrdenEventoPublisher eventoPublisher
    ) {
        this.trabajoRepository = trabajoRepository;
        this.ordenRepository = ordenRepository;
        this.eventoPublisher = eventoPublisher;
    }

    //agregar un nuevo trabajo a una orden
    @Transactional
    public TrabajoResponse agregarTrabajo(
            Long ordenId,
            CrearTrabajoRequest request
    ) {

        Orden orden = buscarOrden(ordenId);

        TrabajoOrden trabajo = new TrabajoOrden(
                orden,
                request.descripcion()
        );

        TrabajoOrden guardado =
                trabajoRepository.save(trabajo);

        return convertirDTO(guardado);
    }

    //listar todos los trabajos dentro de una orden
    @Transactional(readOnly = true)
    public List<TrabajoResponse> listarPorOrden(Long ordenId) {

        buscarOrden(ordenId);

        return trabajoRepository
                .findByOrdenId(ordenId)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // Buscar un trabajo específico
    @Transactional(readOnly = true)
    public TrabajoResponse buscarPorId(
            Long ordenId,
            Long trabajoId
    ) {

        TrabajoOrden trabajo =
                buscarTrabajo(trabajoId);

        validarPerteneceAOrden(
                trabajo,
                ordenId
        );

        return convertirDTO(trabajo);
    }

    // Cambiar estado del trabajo
    @Transactional
    public TrabajoResponse cambiarEstado(
            Long ordenId,
            Long trabajoId,
            EstadoTrabajo nuevoEstado
    ) {

        TrabajoOrden trabajo =
                buscarTrabajo(trabajoId);

        validarPerteneceAOrden(
                trabajo,
                ordenId
        );

        trabajo.setEstado(nuevoEstado);

        return convertirDTO(
                trabajoRepository.save(trabajo)
        );
    }

    // Eliminar trabajo
    @Transactional
    public void eliminarTrabajo(
            Long ordenId,
            Long trabajoId
    ) {

        TrabajoOrden trabajo =
                buscarTrabajo(trabajoId);

        validarPerteneceAOrden(
                trabajo,
                ordenId
        );

        trabajoRepository.delete(trabajo);
    }

    // Comprueba si una orden tiene trabajos pendientes
    @Transactional(readOnly = true)
    public boolean tieneTrabajosPendientes(Long ordenId) {

        return trabajoRepository
                .existsByOrdenIdAndEstadoNot(
                        ordenId,
                        EstadoTrabajo.TERMINADO
                );
    }

    private Orden buscarOrden(Long ordenId) {

        return ordenRepository
                .findById(ordenId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Orden con id " + ordenId + " no encontrada"
                        )
                );
    }

    private TrabajoOrden buscarTrabajo(Long trabajoId) {
        return trabajoRepository
                .findById(trabajoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Trabajo con id " + trabajoId + " no encontrado"
                        )
                );
    }

    private void validarPerteneceAOrden(
            TrabajoOrden trabajo,
            Long ordenId
    ) {

        if (!trabajo.getOrden()
                .getId()
                .equals(ordenId)) {
            throw new IllegalArgumentException(
                    "El trabajo no esta asociado a la orden que se indico"
            );
        }
    }

    private TrabajoResponse convertirDTO(
            TrabajoOrden trabajo
    ) {
        return new TrabajoResponse(
                trabajo.getId(),
                trabajo.getOrden().getId(),
                trabajo.getDescripcion(),
                trabajo.getEstado()
        );
    }

    @Transactional
    public TrabajoResponse actualizar(
            Long ordenId,
            Long trabajoId,
            ActualizarTrabajoRequest request
    ) {

        TrabajoOrden trabajo =
                buscarTrabajo(trabajoId);

        validarPerteneceAOrden(
                trabajo,
                ordenId
        );

        trabajo.setDescripcion(
                request.descripcion()
        );

        return convertirDTO(
                trabajoRepository.save(trabajo)
        );
    }

    @Transactional(readOnly = true)
    public void validarCierreOrden(Long ordenId) {

        if (tieneTrabajosPendientes(ordenId)) {

            throw new ReglaNegocioException(
                    "La orden no puede finalizar porque existen trabajos pendientes"
            );
        }
    }
}