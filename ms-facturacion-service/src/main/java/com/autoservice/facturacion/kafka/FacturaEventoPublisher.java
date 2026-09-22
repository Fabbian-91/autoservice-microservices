package com.autoservice.facturacion.kafka;

import com.autoservice.facturacion.common.exception.EventoKafkaException;
import com.autoservice.facturacion.config.KafkaTopicConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class FacturaEventoPublisher {
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    //Metodo para publicar evento en kafka
    public void publicarFacturacion(FacturaGeneradaEvento evento){
        try {
            String message=objectMapper.writeValueAsString(evento);

            kafkaTemplate.send(KafkaTopicConfig.FACTURACION_TOPIC,message);


        }catch (JacksonException ex){
            throw new EventoKafkaException(
                    "No se pudo publicar el evento de factura generada",
                    ex
            );
        }
    }
}
