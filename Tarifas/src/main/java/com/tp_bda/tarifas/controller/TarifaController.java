package com.tp_bda.tarifas.controller;

import com.tp_bda.tarifas.DTO.TarifaRequestDTO;
import com.tp_bda.tarifas.model.Tarifa;
import com.tp_bda.tarifas.service.TarifaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tarifas")
public class TarifaController {

    @Autowired
    private TarifaService tarifaService;

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
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/aproximada")
    public ResponseEntity<Map<String, Double>> getTarifaAproximada(
            @RequestBody TarifaRequestDTO requestDto) { // Asumo que el DTO que creaste se llama TarifaRequestDto

        try {
            // Llama a un nuevo método de servicio que devuelve un Map
            Map<String, Double> estimaciones = tarifaService.calcularTarifaAproximada(
                    requestDto.distanciaKm(),
                    requestDto.pesoContenedor()
            );
            return ResponseEntity.ok(estimaciones);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Si faltan datos
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
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
            return ResponseEntity.status(400).build();
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