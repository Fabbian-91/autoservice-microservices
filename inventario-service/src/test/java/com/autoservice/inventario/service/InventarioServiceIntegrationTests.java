package com.autoservice.inventario.service;

import com.autoservice.inventario.common.enums.TipoMovimiento;
import com.autoservice.inventario.common.exceptions.StockInsuficienteException;
import com.autoservice.inventario.dto.EntradaInventarioRequestDTO;
import com.autoservice.inventario.dto.MovimientoInventarioResponseDTO;
import com.autoservice.inventario.dto.RepuestoRequestDTO;
import com.autoservice.inventario.dto.RepuestoResponseDTO;
import com.autoservice.inventario.dto.SalidaInventarioRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class InventarioServiceIntegrationTests {

    @Autowired
    private RepuestoService repuestoService;

    @Autowired
    private InventarioService inventarioService;

    @Test
    void registraEntradaYSalidaSinDejarStockNegativo() {
        RepuestoResponseDTO repuesto = repuestoService.crear(
                new RepuestoRequestDTO(
                        "Filtro de aceite",
                        "Filtro para motor",
                        new BigDecimal("4500.00")
                )
        );

        MovimientoInventarioResponseDTO entrada = inventarioService.registrarEntrada(
                repuesto.getId(),
                new EntradaInventarioRequestDTO(10)
        );

        MovimientoInventarioResponseDTO salida = inventarioService.registrarSalida(
                repuesto.getId(),
                new SalidaInventarioRequestDTO(4, 100L)
        );

        assertEquals(TipoMovimiento.ENTRADA, entrada.getTipo());
        assertEquals(TipoMovimiento.SALIDA, salida.getTipo());
        assertEquals(6, repuestoService.obtener(repuesto.getId()).getCantidadDisponible());
        assertTrue(
                inventarioService.consultarDisponibilidad(repuesto.getId(), 6).isDisponible()
        );

        assertThrows(
                StockInsuficienteException.class,
                () -> inventarioService.registrarSalida(
                        repuesto.getId(),
                        new SalidaInventarioRequestDTO(7, 101L)
                )
        );
    }

}
