package com.example.citas_service.publisher;

import com.example.citas_service.event.CitaKafkaEvent;
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

    @Value("${microservices.kafka.topics.citas-events}")
    private String topicoCitasEvents;

    public void publicarEvento(String tipoEvento, Long citaId, String descripcion) {
        log.info("Publicando evento {} en Kafka - citaId: {}", tipoEvento, citaId);
        CitaKafkaEvent event = new CitaKafkaEvent(tipoEvento, citaId, descripcion, java.time.LocalDateTime.now());
        kafkaTemplate.send(topicoCitasEvents, citaId.toString(), event);
    }
}
