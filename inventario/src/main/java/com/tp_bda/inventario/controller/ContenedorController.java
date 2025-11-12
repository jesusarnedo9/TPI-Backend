package com.tp_bda.inventario.controller;

import com.tp_bda.inventario.dto.ContenedorDTO;
import com.tp_bda.inventario.service.CapacidadExcedidaException;
import com.tp_bda.inventario.service.ContenedorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid; // Importante
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contenedores")
public class ContenedorController {

    @Autowired
    private ContenedorService contenedorService;

    // Manejador de excepciones para 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    // Crear un nuevo contenedor
    @PostMapping
    public ResponseEntity<ContenedorDTO> crearContenedor(@Valid @RequestBody ContenedorDTO dto) {
        ContenedorDTO nuevo = contenedorService.guardar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo); // Devuelve 201
    }

    // Obtener un contenedor por su ID
    @GetMapping("/{id}")
    public ResponseEntity<ContenedorDTO> obtenerPorId(@PathVariable Integer id) {
        ContenedorDTO dto = contenedorService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    // Actualizar un contenedor existente
    @PutMapping("/{id}")
    public ResponseEntity<ContenedorDTO> actualizarContenedor(@PathVariable Integer id, @Valid @RequestBody ContenedorDTO dto) {
        ContenedorDTO actualizado = contenedorService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // Listar todos los contenedores
    @GetMapping
    public ResponseEntity<List<ContenedorDTO>> listarContenedores() {
        List<ContenedorDTO> lista = contenedorService.listar();
        return ResponseEntity.ok(lista);
    }

    // Eliminar un contenedor por su ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Devuelve 204
    public ResponseEntity<Void> eliminarContenedor(@PathVariable Integer id) {
        contenedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
    @ExceptionHandler(CapacidadExcedidaException.class)
    public ResponseEntity<?> handleCapacidadExcedida(CapacidadExcedidaException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT) // 409
                .body(Map.of("error", ex.getMessage()));
    }
}