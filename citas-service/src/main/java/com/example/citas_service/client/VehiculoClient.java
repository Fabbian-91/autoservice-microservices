package com.example.citas_service.client;

import com.example.citas_service.dto.VehiculoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vehiculos-service", url = "${microservices.vehiculos.base-url}")
public interface VehiculoClient {

    @GetMapping("/api/vehiculos/{id}")
    VehiculoDTO obtenerVehiculo(@PathVariable("id") Long id);
}
