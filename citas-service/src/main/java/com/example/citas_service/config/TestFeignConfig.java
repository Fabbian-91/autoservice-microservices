package com.example.citas_service.config;

import com.example.citas_service.client.ClienteClient;
import com.example.citas_service.client.VehiculoClient;
import com.example.citas_service.dto.ClienteDTO;
import com.example.citas_service.dto.VehiculoDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestFeignConfig {

    @Bean
    @Primary
    public ClienteClient clienteClientMock() {
        return new ClienteClient() {
            @Override
            public ClienteDTO obtenerCliente(Long id) {
                ClienteDTO cliente = new ClienteDTO();
                cliente.setId(id);
                cliente.setNombre("Juan Perez");
                cliente.setTelefono("8888-1234");
                cliente.setEmail("juan@mail.com");
                return cliente;
            }
        };
    }

    @Bean
    @Primary
    public VehiculoClient vehiculoClientMock() {
        return new VehiculoClient() {
            @Override
            public VehiculoDTO obtenerVehiculo(Long id) {
                VehiculoDTO vehiculo = new VehiculoDTO();
                vehiculo.setId(id);
                vehiculo.setPlaca("ABC-123");
                vehiculo.setMarca("Toyota");
                vehiculo.setModelo("Corolla");
                vehiculo.setClienteId(1L);
                return vehiculo;
            }
        };
    }
}
