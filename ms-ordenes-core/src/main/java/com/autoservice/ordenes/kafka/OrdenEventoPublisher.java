package com.autoservice.ordenes.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrdenEventoPublisher {

    private final KafkaTemplate<String, OrdenEvento> kafkaTemplate;

    private final String topic;

    public OrdenEventoPublisher(
            KafkaTemplate<String, OrdenEvento> kafkaTemplate,
            @Value("${app.kafka.topic.ordenes}") String topic
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publicar(
            String tipo,
            Long ordenId,
            String descripcion
    ) {

        OrdenEvento evento =
                new OrdenEvento(
                        tipo,
                        ordenId,
                        descripcion,
                        LocalDateTime.now()
                );

        kafkaTemplate.send(
                topic,
                ordenId.toString(),
                evento
        );
    }
}