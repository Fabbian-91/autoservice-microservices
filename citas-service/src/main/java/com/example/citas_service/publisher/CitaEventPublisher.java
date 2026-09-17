package com.example.citas_service.publisher;

import com.example.citas_service.event.CitaCreadaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CitaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${microservices.kafka.topics.cita-creada}")
    private String topicoCitaCreada;

    public void publicarCitaCreada(CitaCreadaEvent event) {
        log.info("Publicando evento CITA_CREADA en Kafka - citaId: {}", event.getCitaId());
        kafkaTemplate.send(topicoCitaCreada, event.getCitaId().toString(), event);
    }
}
