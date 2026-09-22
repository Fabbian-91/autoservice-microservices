package com.taller.ms_historial_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String CITAS_TOPIC = "citas-events";
    public static final String ORDENES_TOPIC = "ordenes-events";
    public static final String FACTURACION_TOPIC = "facturacion-events";

    // Topic para los eventos de citas
    @Bean
    public NewTopic citasTopic() {
        return TopicBuilder
                .name(CITAS_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    // Topic para los eventos de órdenes
    @Bean
    public NewTopic ordenesTopic() {
        return TopicBuilder
                .name(ORDENES_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    // Topic para los eventos de facturación
    @Bean
    public NewTopic facturacionTopic() {
        return TopicBuilder
                .name(FACTURACION_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}