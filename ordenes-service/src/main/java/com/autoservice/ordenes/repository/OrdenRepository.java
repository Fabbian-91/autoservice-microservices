package com.autoservice.ordenes.repository;

import com.autoservice.ordenes.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
}
