package com.autoservice.facturacion.client;

import com.autoservice.facturacion.dto.OrdenResponseDTO;
import com.autoservice.facturacion.dto.RepuestoOrdenDTO;
import com.autoservice.facturacion.dto.TrabajoOrdenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ordenes-service", url = "${clientes.ordenes.url}")
public interface OrdenClient {

    @GetMapping("/api/ordenes/{id}")
    OrdenResponseDTO obtenerOrden(@PathVariable("id") Long id);

    @GetMapping("/api/ordenes/{ordenId}/trabajos")
    List<TrabajoOrdenDTO> obtenerTrabajos(@PathVariable("ordenId") Long ordenId);

    @GetMapping("/api/ordenes/{ordenId}/repuestos")
    List<RepuestoOrdenDTO> obtenerRepuestos(@PathVariable("ordenId") Long ordenId);
}
