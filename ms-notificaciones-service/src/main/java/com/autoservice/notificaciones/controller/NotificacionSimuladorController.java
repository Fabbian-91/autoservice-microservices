package com.autoservice.notificaciones.controller;

import com.autoservice.notificaciones.dto.CitaRegistradaEventoDTO;
import com.autoservice.notificaciones.dto.FacturaGeneradaEventoDTO;
import com.autoservice.notificaciones.dto.VehiculoListoEventoDTO;
import com.autoservice.notificaciones.publisher.NotificacionPublisher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Endpoints temporales: hacen lo que Citas/Ordenes/Facturacion van a hacer cuando existan.
// Se borran cuando esos microservicios ya publiquen sus propios eventos.
@RestController
@RequestMapping("/api/notificaciones/simular")
@RequiredArgsConstructor
public class NotificacionSimuladorController {

    private final NotificacionPublisher notificacionPublisher;

    @PostMapping("/cita-registrada")
    public ResponseEntity<Void> simularCitaRegistrada(@Valid @RequestBody CitaRegistradaEventoDTO evento) {
        notificacionPublisher.publicarCitaRegistrada(evento);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/vehiculo-listo")
    public ResponseEntity<Void> simularVehiculoListo(@Valid @RequestBody VehiculoListoEventoDTO evento) {
        notificacionPublisher.publicarVehiculoListo(evento);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/factura-generada")
    public ResponseEntity<Void> simularFacturaGenerada(@Valid @RequestBody FacturaGeneradaEventoDTO evento) {
        notificacionPublisher.publicarFacturaGenerada(evento);
        return ResponseEntity.accepted().build();
    }
}
