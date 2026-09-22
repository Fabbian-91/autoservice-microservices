package com.taller.ms_clientes_vehiculos.repository;

import com.taller.ms_clientes_vehiculos.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCedula(String cedula);
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByCedula(String cedula);
}