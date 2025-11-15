package com.tp_bda.usuarios.controller;

import com.tp_bda.usuarios.dto.TransportistaDTO;
import com.tp_bda.usuarios.service.TransportistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transportistas")
public class TransportistaController {

    @Autowired
    private TransportistaService transportistaService;

    private static final Logger log = LoggerFactory.getLogger(TransportistaController.class);

    // Crear un nuevo transportista
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody TransportistaDTO transportistaDTO) {
        try {
            TransportistaDTO nuevo = transportistaService.guardar(transportistaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos al crear transportista: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error creando transportista", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    // Obtener transportista por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            TransportistaDTO dto = transportistaService.buscarPorId(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error obteniendo transportista {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    // Consultar el perfil del transportista autenticado (Mejor práctica de seguridad)
    // Permite al transportista ver su perfil usando su token, sin pasar el ID.
    @PreAuthorize("hasAuthority('TRANSPORTISTA')")
    @GetMapping("/mi-perfil")
    public ResponseEntity<?> obtenerMiPerfil(@AuthenticationPrincipal Jwt jwt) {
        try {
            String keycloakUserId = jwt.getSubject();
            TransportistaDTO dto = transportistaService.buscarPorKeycloakId(keycloakUserId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error obteniendo perfil del transportista autenticado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    // Actualizar transportista
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody TransportistaDTO transportistaDTO) {
        try {
            TransportistaDTO actualizado = transportistaService.actualizar(id, transportistaDTO);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos al actualizar transportista {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error actualizando transportista {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    // Listar todos los transportistas
    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<TransportistaDTO> list = transportistaService.listar();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("Error listando transportistas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }
}
