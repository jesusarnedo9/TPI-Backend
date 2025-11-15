package com.tp_bda.tarifas.controller;

import com.tp_bda.tarifas.DTO.TarifaRequestDTO;
import com.tp_bda.tarifas.model.Tarifa;
import com.tp_bda.tarifas.service.TarifaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tarifas")
public class TarifaController {

    @Autowired
    private TarifaService tarifaService;

    private static final Logger log = LoggerFactory.getLogger(TarifaController.class);

    // ----------------------------------------------------------------------
    // ENDPOINT 1: ESTIMACIÓN (Consumido por MS Solicitudes)
    // ----------------------------------------------------------------------

    /**
     * Endpoint: GET /api/tarifas/estimar
     * Calcula el costo estimado en base a los parámetros de la URL.
     */
    @GetMapping("/estimar")
    public ResponseEntity<Double> estimarTarifa(
            @RequestParam Double distancia,
            @RequestParam Double peso,
            @RequestParam Double volumen
    ) {
        try {
            Double costoEstimado = tarifaService.estimarCosto(distancia, peso, volumen);
            return ResponseEntity.ok(costoEstimado);
        } catch (IllegalArgumentException e) {
            log.warn("Estimación inválida: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("Error estimando tarifa", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/aproximada")
    public ResponseEntity<?> getTarifaAproximada(
            @RequestBody TarifaRequestDTO requestDto) { // Asumo que el DTO que creaste se llama TarifaRequestDto

        try {
            // Llama a un nuevo método de servicio que devuelve un Map
            Map<String, Double> estimaciones = tarifaService.calcularTarifaAproximada(
                    requestDto.distanciaKm(),
                    requestDto.pesoContenedor()
            );
            return ResponseEntity.ok(estimaciones);
        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos en tarifación aproximada: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error calculando tarifa aproximada", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    // ----------------------------------------------------------------------
    // ENDPOINT 2: GESTIÓN DE PARÁMETROS BASE (Consumido por Operador/Admin)
    // ----------------------------------------------------------------------

    /**
     * Endpoint: POST /api/tarifas/param
     * Crea o actualiza una tarifa base (ej. "COSTO_KM").
     */
    @PostMapping("/param")
    public ResponseEntity<Tarifa> crearOActualizarTarifa(@RequestBody Tarifa tarifa) {
        try {
            Tarifa nuevaTarifa = tarifaService.guardarTarifa(tarifa);
            return ResponseEntity.ok(nuevaTarifa);
        } catch (Exception e) {
            log.error("Error creando/actualizando tarifa", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * Endpoint: GET /api/tarifas/param
     * Consulta todas las tarifas base existentes.
     */
    @GetMapping("/param")
    public ResponseEntity<List<Tarifa>> obtenerTodasLasTarifas() {
        List<Tarifa> tarifas = tarifaService.obtenerTodasLasTarifas();
        return ResponseEntity.ok(tarifas);
    }
}