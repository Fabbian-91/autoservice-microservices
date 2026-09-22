package com.autoservice.facturacion.rabbit;

import com.autoservice.facturacion.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacturaNotificacionPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publicarFacturaGenerada(
            FacturaGeneradaEventoDTO evento
    ) {

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY_FACTURA_GENERADA,
                evento
        );
    }
}
