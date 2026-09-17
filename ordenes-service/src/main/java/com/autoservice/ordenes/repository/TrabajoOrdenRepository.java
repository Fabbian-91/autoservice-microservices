package com.autoservice.ordenes.repository;

import com.autoservice.ordenes.model.EstadoTrabajo;
import com.autoservice.ordenes.model.TrabajoOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrabajoOrdenRepository
        extends JpaRepository<TrabajoOrden, Long> {

    // Lista todos los trabajos pertenecientes a una orden
    List<TrabajoOrden> findByOrdenId(Long ordenId);

    // Indica si existe algún trabajo que NO esté terminado
    boolean existsByOrdenIdAndEstadoNot(
            Long ordenId,
            EstadoTrabajo estado
    );
}