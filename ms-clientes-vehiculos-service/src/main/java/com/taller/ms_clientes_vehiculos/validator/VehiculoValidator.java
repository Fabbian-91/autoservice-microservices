package com.taller.ms_clientes_vehiculos.validator;

import com.taller.ms_clientes_vehiculos.common.exception.DuplicatePlacaException;
import com.taller.ms_clientes_vehiculos.repository.VehiculoRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;


@Component
@Data
@AllArgsConstructor
public class VehiculoValidator {

    private final VehiculoRepository vehiculoRepository;


    //reglas de negocio...
    public void checkPlacaExists(String placa) {

        if (vehiculoRepository.existsByPlaca(placa)) {
            throw new DuplicatePlacaException(placa);
        }
    }

    public void checkPlacaExistsForUpdate(String placa, Long id) {

        if (vehiculoRepository.existsByPlacaAndIdNot(placa, id)) {
            throw new DuplicatePlacaException(placa);
        }
    }


}
