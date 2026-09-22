package com.autoservice.notificaciones.listener;

import com.autoservice.notificaciones.config.RabbitConfig;
import com.autoservice.notificaciones.dto.CitaRegistradaEventoDTO;
import com.autoservice.notificaciones.dto.FacturaGeneradaEventoDTO;
import com.autoservice.notificaciones.dto.VehiculoListoEventoDTO;
import com.autoservice.notificaciones.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificacionListener {

    private final NotificacionService notificacionService;

    @RabbitListener(queues = RabbitConfig.QUEUE_CITA_REGISTRADA)
    public void escucharCitaRegistrada(CitaRegistradaEventoDTO evento) {
        notificacionService.procesarCitaRegistrada(evento);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_VEHICULO_LISTO)
    public void escucharVehiculoListo(VehiculoListoEventoDTO evento) {
        notificacionService.procesarVehiculoListo(evento);
    }

    @RabbitListener(queues = RabbitConfig.QUEUE_FACTURA_GENERADA)
    public void escucharFacturaGenerada(FacturaGeneradaEventoDTO evento) {
        notificacionService.procesarFacturaGenerada(evento);
    }
}
