package com.autoservice.facturacion.repository;

import com.autoservice.facturacion.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    boolean existsByOrdenId(Long ordenId);

    List<Factura> findByClienteId(Long clienteId);
}
