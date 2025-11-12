package com.tp_bda.Viajes.web.api.controllers;

import com.tp_bda.Viajes.service.ViajesService;
import com.tp_bda.Viajes.web.api.dto.RutaTentativaDto;
import com.tp_bda.Viajes.web.api.dto.TramoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/viajes")
public class ViajesApi {

    @Autowired
    private ViajesService service;

    // POST /rutas/{tramoId} (Asignar Camión a Tramo)
    @PostMapping("/rutas/{tramoId}")
    public ResponseEntity<?> asignarCamion(
            @PathVariable Integer tramoId,
            @RequestBody Map<String, Object> request) {

        // Extracción y conversión segura de los datos del cuerpo de la petición
        Integer camionId = (Integer) request.get("camionId");
        // Aseguramos que se lea como Double, incluso si viene como Integer en el JSON
        Double pesoContenedor = ((Number) request.getOrDefault("pesoContenedor", 0.0)).doubleValue();
        Double volumenContenedor = ((Number) request.getOrDefault("volumenContenedor", 0.0)).doubleValue();

        try {
            TramoDto tramoActualizado = service.asignarCamionATramo(tramoId, camionId, pesoContenedor, volumenContenedor);
            // Respuesta: Tramo actualizado (estado asignado)
            return ResponseEntity.ok(tramoActualizado);
        } catch (IllegalArgumentException e) {
            // 409 CONFLICT: Error de validación (camión no apto)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            // 404 NOT FOUND: Error genérico (ej. tramo no encontrado)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    // PUT /tramos/{tramoid}/inicio
    @PutMapping("/tramos/{tramoId}/inicio")
    public ResponseEntity<?> iniciarTramo(@PathVariable Integer tramoId) {
        try {
            TramoDto tramo = service.iniciarTramo(tramoId);
            // Respuesta: Tramo actualizado (estado: en tránsito)
            return ResponseEntity.ok(tramo);
        } catch (IllegalStateException e) {
            // 409 CONFLICT: Si el estado no permite la acción
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            // 404 NOT FOUND: Si el tramo no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    // PUT /tramos/{tramoid}/fin
    @PutMapping("/tramos/{tramoId}/fin")
    public ResponseEntity<?> finalizarTramo(@PathVariable Integer tramoId) {
        try {
            TramoDto tramo = service.finalizarTramo(tramoId);
            // Respuesta: Tramo actualizado (estado: finalizado)
            return ResponseEntity.ok(tramo);
        } catch (IllegalStateException e) {
            // 409 CONFLICT: Si el estado no permite la acción
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            // 404 NOT FOUND: Si el tramo no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }
    // GET /rutas/tentativas
    @GetMapping("/rutas/tentativas")
    public ResponseEntity<?> getRutasTentativas(
            @RequestParam String origen,
            @RequestParam double latOrigen,
            @RequestParam double lonOrigen,
            @RequestParam String destino,
            @RequestParam double latDestino,
            @RequestParam double lonDestino,
            @RequestParam double pesoContenedor)
    {
        try {
            RutaTentativaDto ruta = service.getRutaTentativa(
                    origen, latOrigen, lonOrigen,
                    destino, latDestino, lonDestino,
                    pesoContenedor
            );
            return ResponseEntity.ok(ruta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // GET /rutas/asignadas/transportista/{id}
    @GetMapping("/rutas/asignadas/transportista/{transportistaId}")
    public ResponseEntity<?> getRutasAsignadas(@PathVariable Integer transportistaId) {
        try {
            List<TramoDto> tramos = service.getTramosAsignadosPorTransportista(transportistaId);

            if (tramos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            // Respuesta: Lista de tramos asignados.
            return ResponseEntity.ok(tramos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error al consultar tramos asignados: " + e.getMessage()));
        }
    }
}