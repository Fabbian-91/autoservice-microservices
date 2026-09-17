package com.autoservice.facturacion.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    // provisorio, falta definicion oficial de topics
    public static final String TOPIC_FACTURA_GENERADA = "factura-generada";

    @Bean
    public NewTopic facturaGeneradaTopic() {
        return TopicBuilder.name(TOPIC_FACTURA_GENERADA).build();
    }
}
