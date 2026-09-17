package com.example.citas_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${microservices.rabbitmq.queues.cita-registrada}")
    private String colaCitaRegistrada;

    @Value("${microservices.rabbitmq.exchanges.cita}")
    private String exchangeCita;

    @Value("${microservices.rabbitmq.bindings.cita-registrada}")
    private String bindingCitaRegistrada;

    @Bean
    public Queue colaCitaRegistrada() {
        return new Queue(colaCitaRegistrada, true);
    }

    @Bean
    public DirectExchange exchangeCita() {
        return new DirectExchange(exchangeCita);
    }

    @Bean
    public Binding bindingCitaRegistrada(Queue colaCitaRegistrada, DirectExchange exchangeCita) {
        return BindingBuilder.bind(colaCitaRegistrada).to(exchangeCita).with(bindingCitaRegistrada);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
