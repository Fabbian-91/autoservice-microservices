package com.autoservice.inventario.repository;

import com.autoservice.inventario.model.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoInventarioRepository
        extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findAllByOrderByFechaDesc();

    List<MovimientoInventario> findByRepuestoIdOrderByFechaDesc(Long repuestoId);

    List<MovimientoInventario> findByOrdenIdOrderByFechaDesc(Long ordenId);

    boolean existsByRepuestoId(Long repuestoId);

}
