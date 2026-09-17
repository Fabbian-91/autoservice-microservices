package com.taller.ms_historial_service.repository;

import com.taller.ms_historial_service.enums.TipoEvento;
import com.taller.ms_historial_service.model.EventoHistorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoHistorialRepository extends JpaRepository<EventoHistorial,Long> {
    List<EventoHistorial> findAllByOrderByFechaDesc();

    List<EventoHistorial> findByTipoEventoOrderByFechaDesc(TipoEvento tipoEvento);

    List<EventoHistorial> findByEntidadIdOrderByFechaDesc(Long entidadId);
}
