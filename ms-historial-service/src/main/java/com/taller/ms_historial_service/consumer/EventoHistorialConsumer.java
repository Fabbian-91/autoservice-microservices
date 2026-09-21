package com.taller.ms_historial_service.consumer;

import com.taller.ms_historial_service.common.exception.EventoKafkaException;
import com.taller.ms_historial_service.config.KafkaTopicConfig;
import com.taller.ms_historial_service.dto.EventoKafkaDTO;
import com.taller.ms_historial_service.service.contract.IEventoHistorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class EventoHistorialConsumer {

    //Inyección de dependecias
    private final IEventoHistorialService eventoHistorialService;
    private final ObjectMapper objectMapper;

    //Lista de topics
    @KafkaListener(
            topics = {
                    KafkaTopicConfig.CITAS_TOPIC,
                    KafkaTopicConfig.ORDENES_TOPIC,
                    KafkaTopicConfig.FACTURACION_TOPIC
            },
            groupId = "historial-group"
    )
    public void consumirEvento(String mensaje) {

        try {

            // Convierte el JSON a DTO
            EventoKafkaDTO eventoKafkaDTO =
                    objectMapper.readValue(
                            mensaje,
                            EventoKafkaDTO.class
                    );

            // Guarda el evento y su JSON original
            eventoHistorialService.guardarEvento(
                    eventoKafkaDTO,
                    mensaje
            );

        } catch (JacksonException ex) {

            throw new EventoKafkaException(
                    "No se pudo convertir el evento recibido desde Kafka",
                    ex
            );
        }
    }
}