package com.example.citas_service.repository;

import com.example.citas_service.model.Cita;
import com.example.citas_service.model.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByClienteIdAndEstado(Long clienteId, EstadoCita estado);

    List<Cita> findByFechaAndEstado(LocalDate fecha, EstadoCita estado);

    long countByClienteIdAndEstado(Long clienteId, EstadoCita estado);
}
