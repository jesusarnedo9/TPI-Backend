package com.tp_bda.inventario.controller;

import com.tp_bda.inventario.dto.DepositoDTO;
import com.tp_bda.inventario.service.DepositoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid; // Importante
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/depositos")
public class DepositoController {

    @Autowired
    private DepositoService depositoService;

    // Manejador de excepciones para 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    // Crear un nuevo depósito
    @PostMapping
    public ResponseEntity<DepositoDTO> crearDeposito(@Valid @RequestBody DepositoDTO dto) {
        DepositoDTO nuevo = depositoService.guardar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo); // Devuelve 201
    }

    // Obtener un depósito por su ID
    @GetMapping("/{id}")
    public ResponseEntity<DepositoDTO> obtenerPorId(@PathVariable Integer id) {
        DepositoDTO dto = depositoService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    // Actualizar un depósito existente
    @PutMapping("/{id}")
    public ResponseEntity<DepositoDTO> actualizarDeposito(@PathVariable Integer id, @Valid @RequestBody DepositoDTO dto) {
        DepositoDTO actualizado = depositoService.actualizar(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    // Listar todos los depósitos
    @GetMapping
    public ResponseEntity<List<DepositoDTO>> listarDepositos() {
        List<DepositoDTO> lista = depositoService.listar();
        return ResponseEntity.ok(lista);
    }

    // Eliminar un depósito por su ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Devuelve 204
    public ResponseEntity<Void> eliminarDeposito(@PathVariable Integer id) {
        depositoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}