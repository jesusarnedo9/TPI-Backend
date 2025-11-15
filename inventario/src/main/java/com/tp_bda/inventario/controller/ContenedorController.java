package com.tp_bda.inventario.controller;

import com.tp_bda.inventario.dto.ContenedorDTO;
import com.tp_bda.inventario.service.CapacidadExcedidaException;
import com.tp_bda.inventario.service.ContenedorService;
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
@RequestMapping("/contenedores")
public class ContenedorController {

    @Autowired
    private ContenedorService contenedorService;

    private static final Logger log = LoggerFactory.getLogger(ContenedorController.class);

    // Manejador de excepciones para 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of("message", ex.getMessage()));
    }

    // Crear un nuevo contenedor
    @PostMapping
    public ResponseEntity<?> crearContenedor(@Valid @RequestBody ContenedorDTO dto) {
        try {
            ContenedorDTO nuevo = contenedorService.guardar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos al crear contenedor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error creando contenedor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    // Obtener un contenedor por su ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            ContenedorDTO dto = contenedorService.buscarPorId(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return handleEntityNotFound(e);
        } catch (Exception e) {
            log.error("Error obteniendo contenedor {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    // Actualizar un contenedor existente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarContenedor(@PathVariable Integer id, @Valid @RequestBody ContenedorDTO dto) {
        try {
            ContenedorDTO actualizado = contenedorService.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (EntityNotFoundException e) {
            return handleEntityNotFound(e);
        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos al actualizar contenedor {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error actualizando contenedor {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    // Listar todos los contenedores
    @GetMapping
    public ResponseEntity<?> listarContenedores() {
        try {
            List<ContenedorDTO> lista = contenedorService.listar();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            log.error("Error listando contenedores", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    // Eliminar un contenedor por su ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Devuelve 204
    public ResponseEntity<?> eliminarContenedor(@PathVariable Integer id) {
        try {
            contenedorService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return handleEntityNotFound(e);
        } catch (Exception e) {
            log.error("Error eliminando contenedor {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }
    @ExceptionHandler(CapacidadExcedidaException.class)
    public ResponseEntity<?> handleCapacidadExcedida(CapacidadExcedidaException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT) // 409
                .body(Map.of("message", ex.getMessage()));
    }
}