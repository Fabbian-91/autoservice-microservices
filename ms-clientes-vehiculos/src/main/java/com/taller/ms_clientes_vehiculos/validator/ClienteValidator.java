package com.taller.ms_clientes_vehiculos.validator;

import com.taller.ms_clientes_vehiculos.common.exception.CedulaDuplicada;
import com.taller.ms_clientes_vehiculos.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClienteValidator {


    //inyecion de dependencia

    private final ClienteRepository clienteRepository;

    //validar la cedula
    public void validarCedulaUnica(String cedula) {
        if (clienteRepository.existsByCedula(cedula)) {
            throw new CedulaDuplicada(cedula);
        }
    }
}
