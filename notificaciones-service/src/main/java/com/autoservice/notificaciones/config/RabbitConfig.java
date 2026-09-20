package com.autoservice.notificaciones.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "notificaciones.exchange";

    public static final String QUEUE_CITA_REGISTRADA = "notificaciones.cita-registrada";
    public static final String QUEUE_VEHICULO_LISTO = "notificaciones.vehiculo-listo";
    public static final String QUEUE_FACTURA_GENERADA = "notificaciones.factura-generada";

    public static final String ROUTING_KEY_CITA_REGISTRADA = "cita.registrada";
    public static final String ROUTING_KEY_VEHICULO_LISTO = "vehiculo.listo";
    public static final String ROUTING_KEY_FACTURA_GENERADA = "factura.generada";

    @Bean
    public TopicExchange notificacionesExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue citaRegistradaQueue() {
        return new Queue(QUEUE_CITA_REGISTRADA, true);
    }

    @Bean
    public Queue vehiculoListoQueue() {
        return new Queue(QUEUE_VEHICULO_LISTO, true);
    }

    @Bean
    public Queue facturaGeneradaQueue() {
        return new Queue(QUEUE_FACTURA_GENERADA, true);
    }

    @Bean
    public Binding citaRegistradaBinding(Queue citaRegistradaQueue, TopicExchange notificacionesExchange) {
        return BindingBuilder.bind(citaRegistradaQueue).to(notificacionesExchange).with(ROUTING_KEY_CITA_REGISTRADA);
    }

    @Bean
    public Binding vehiculoListoBinding(Queue vehiculoListoQueue, TopicExchange notificacionesExchange) {
        return BindingBuilder.bind(vehiculoListoQueue).to(notificacionesExchange).with(ROUTING_KEY_VEHICULO_LISTO);
    }

    @Bean
    public Binding facturaGeneradaBinding(Queue facturaGeneradaQueue, TopicExchange notificacionesExchange) {
        return BindingBuilder.bind(facturaGeneradaQueue).to(notificacionesExchange).with(ROUTING_KEY_FACTURA_GENERADA);
    }

    // Sin esto, Spring manda los mensajes serializados en binario (formato propio de Java).
    // Con JSON, cualquier lenguaje/servicio puede leer lo que publicamos.
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
