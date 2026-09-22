package com.autoservice.ordenes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${services.vehiculos.url}")
    private String servicesVehiculosUrl;

    @Value("${services.clientes.url}")
    private String servicesClientesUrl;

    @Bean
    public RestClient vehiculoRestClient() {
        return RestClient.builder()
                .baseUrl(servicesVehiculosUrl)
                .build();
    }

    @Bean
    public RestClient clienteRestClient() {
        return RestClient.builder()
                .baseUrl(servicesClientesUrl)
                .build();
    }

}