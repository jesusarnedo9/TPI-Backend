package com.tp_bda.inventario.controller;

import com.tp_bda.inventario.dto.CamionDTO;
import com.tp_bda.inventario.service.CamionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

@RestController
@RequestMapping("/camiones")
public class CamionController {

    @Autowired
    private CamionService camionService;

    private static final Logger log = LoggerFactory.getLogger(CamionController.class);

    @PostMapping
    public ResponseEntity<?> crearCamion(@RequestBody CamionDTO dto) {
        try {
            CamionDTO nuevo = camionService.guardar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos al crear camión: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error creando camión", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            CamionDTO dto = camionService.buscarPorId(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error obteniendo camión {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }
    @GetMapping("/{id}/validar")
    public ResponseEntity<?> validarCapacidad(
            @PathVariable Integer id,
            @RequestParam BigDecimal peso,
            @RequestParam BigDecimal volumen) {
        try {
            camionService.validarCapacidad(id, peso, volumen);
            return ResponseEntity.ok(true);
        } catch (IllegalArgumentException e) {
            log.warn("Validación inválida para camión {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error validando capacidad de camión {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCamion(@PathVariable Integer id, @RequestBody CamionDTO dto) {
        try {
            CamionDTO actualizado = camionService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos al actualizar camión {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error actualizando camión {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    @GetMapping
    public ResponseEntity<?> listarCamiones() {
        try {
            List<CamionDTO> list = camionService.listar();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("Error listando camiones", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }
}
