package com.example.citas_service.publisher;

import com.example.citas_service.dto.CitaRegistradaEventoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CitaNotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${microservices.rabbitmq.exchanges.notificaciones}")
    private String exchangeNotificaciones;

    @Value("${microservices.rabbitmq.bindings.cita-registrada}")
    private String routingKeyCitaRegistrada;

    public void notificarCitaCreada(Long clienteId, LocalDate fecha, LocalTime hora) {
        log.info("Enviando notificacion de cita creada a RabbitMQ - clienteId: {}", clienteId);
        CitaRegistradaEventoDTO evento = new CitaRegistradaEventoDTO(clienteId, fecha, hora);
        rabbitTemplate.convertAndSend(exchangeNotificaciones, routingKeyCitaRegistrada, evento);
    }
}
