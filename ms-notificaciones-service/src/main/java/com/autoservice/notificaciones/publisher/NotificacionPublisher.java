package com.autoservice.notificaciones.publisher;

import com.autoservice.notificaciones.config.RabbitConfig;
import com.autoservice.notificaciones.dto.CitaRegistradaEventoDTO;
import com.autoservice.notificaciones.dto.FacturaGeneradaEventoDTO;
import com.autoservice.notificaciones.dto.VehiculoListoEventoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

// Simula lo que Citas/Ordenes/Facturacion van a publicar cuando existan.
// El dia que se conecten de verdad, esta clase (y su controller) se puede dar de baja sin tocar el listener.
@Component
@RequiredArgsConstructor
public class NotificacionPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publicarCitaRegistrada(CitaRegistradaEventoDTO evento) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY_CITA_REGISTRADA, evento);
    }

    public void publicarVehiculoListo(VehiculoListoEventoDTO evento) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY_VEHICULO_LISTO, evento);
    }

    public void publicarFacturaGenerada(FacturaGeneradaEventoDTO evento) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY_FACTURA_GENERADA, evento);
    }
}
