package com.example.citas_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${microservices.rabbitmq.queues.cita-registrada}")
    private String colaCitaRegistrada;

    @Value("${microservices.rabbitmq.exchanges.notificaciones}")
    private String exchangeNotificaciones;

    @Value("${microservices.rabbitmq.bindings.cita-registrada}")
    private String bindingCitaRegistrada;

    @Bean
    public Queue colaCitaRegistrada() {
        return new Queue(colaCitaRegistrada, true);
    }

    @Bean
    public TopicExchange notificacionesExchange() {
        return new TopicExchange(exchangeNotificaciones);
    }

    @Bean
    public Binding bindingCitaRegistrada(Queue colaCitaRegistrada, TopicExchange notificacionesExchange) {
        return BindingBuilder.bind(colaCitaRegistrada).to(notificacionesExchange).with(bindingCitaRegistrada);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
