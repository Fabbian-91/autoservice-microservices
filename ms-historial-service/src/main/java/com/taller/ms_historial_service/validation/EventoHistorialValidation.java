package com.taller.ms_historial_service.validation;

import com.taller.ms_historial_service.common.exception.EventoKafkaException;
import com.taller.ms_historial_service.dto.EventoKafkaDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EventoHistorialValidation {

    private final Validator validator;

    public void validarEvento(EventoKafkaDTO evento) {

        // Evita procesar un evento inexistente
        if (evento == null) {
            throw new EventoKafkaException(
                    "El evento recibido desde Kafka no puede ser nulo"
            );
        }

        // Ejecuta las validaciones Jakarta del DTO
        Set<ConstraintViolation<EventoKafkaDTO>> violations =
                validator.validate(evento);

        // Obtiene todos los errores encontrados
        if (!violations.isEmpty()) {

            List<String> errores = violations.stream()
                    .map(error ->
                            error.getPropertyPath()
                                    + ": "
                                    + error.getMessage()
                    )
                    .toList();

            // Detiene el procesamiento de un evento inválido
            throw new EventoKafkaException(
                    String.join(", ", errores)
            );
        }
    }
}