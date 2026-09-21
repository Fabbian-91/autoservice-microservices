package com.autoservice.ordenes.repository;

import com.autoservice.ordenes.model.Orden;
import com.autoservice.ordenes.model.RepuestoOrden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepuestoOrdenRepository extends JpaRepository<RepuestoOrden, Long> {

    List<RepuestoOrden> findByOrdenId(Long ordenId);
}
