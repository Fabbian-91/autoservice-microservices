package com.autoservice.ordenes.client;

import com.autoservice.ordenes.dto.VehiculoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class VehiculoClient {

    private final RestClient vehiculoRestClient;

    //GET a ms-vehiculos: me devuelve el vehiculo o propaga su 404
    public VehiculoDTO obtenerVehiculo(Long id) {
        return vehiculoRestClient.get()
                .uri("/api/vehiculos/{id}", id)
                .retrieve()
                .body(VehiculoDTO.class);
    }

}