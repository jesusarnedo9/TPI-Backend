package com.tp_bda.inventario.controller;

import com.tp_bda.inventario.dto.DepositoDTO;
import com.tp_bda.inventario.service.DepositoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid; // Importante
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/depositos")
public class DepositoController {

    @Autowired
    private DepositoService depositoService;

    private static final Logger log = LoggerFactory.getLogger(DepositoController.class);

    // Manejador de excepciones para 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of("message", ex.getMessage()));
    }

    // Crear un nuevo depósito
    @PostMapping
    public ResponseEntity<?> crearDeposito(@Valid @RequestBody DepositoDTO dto) {
        try {
            DepositoDTO nuevo = depositoService.guardar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos al crear depósito: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error creando depósito", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    // Obtener un depósito por su ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            DepositoDTO dto = depositoService.buscarPorId(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return handleEntityNotFound(e);
        } catch (Exception e) {
            log.error("Error obteniendo depósito {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    // Actualizar un depósito existente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDeposito(@PathVariable Integer id, @Valid @RequestBody DepositoDTO dto) {
        try {
            DepositoDTO actualizado = depositoService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return handleEntityNotFound(e);
        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos al actualizar depósito {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error actualizando depósito {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    // Listar todos los depósitos
    @GetMapping
    public ResponseEntity<?> listarDepositos() {
        try {
            List<DepositoDTO> lista = depositoService.listar();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error listando depósitos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    // Eliminar un depósito por su ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Devuelve 204
    public ResponseEntity<?> eliminarDeposito(@PathVariable Integer id) {
        try {
            depositoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return handleEntityNotFound(e);
        } catch (Exception e) {
            log.error("Error eliminando depósito {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }
}