package com.taller.ms_clientes_vehiculos.common.exception;

import com.taller.ms_clientes_vehiculos.dto.VehiculoRequestDTO;

public class DuplicatePlacaException extends RuntimeException{

    public DuplicatePlacaException(String placa){
        super("La placa " + placa + " ya existe en la base de datos. Por favor, verifique la información e intente nuevamente.");
    }

}
