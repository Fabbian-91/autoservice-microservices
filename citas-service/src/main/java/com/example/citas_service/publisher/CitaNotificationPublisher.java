package com.example.citas_service.publisher;

import com.example.citas_service.event.CitaCreadaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CitaNotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${microservices.rabbitmq.exchanges.cita}")
    private String exchangeCita;

    @Value("${microservices.rabbitmq.bindings.cita-registrada}")
    private String bindingCitaRegistrada;

    public void notificarCitaCreada(CitaCreadaEvent event) {
        log.info("Enviando notificacion de cita creada a RabbitMQ - citaId: {}", event.getCitaId());
        rabbitTemplate.convertAndSend(exchangeCita, bindingCitaRegistrada, event);
    }
}
