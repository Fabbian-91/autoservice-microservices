package com.autoservice.ordenes.client;

import com.autoservice.ordenes.dto.ClienteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ClienteClient {

    private final RestClient clienteRestClient;

    //GET a ms-clientes: me devuelve el cliente o propaga su 404
    public ClienteDTO obtenerCliente(Long id) {
        return clienteRestClient.get()
                .uri("/api/clientes/{id}", id)
                .retrieve()
                .body(ClienteDTO.class);
    }

}