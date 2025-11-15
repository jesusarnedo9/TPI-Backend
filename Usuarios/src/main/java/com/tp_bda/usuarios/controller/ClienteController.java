package com.tp_bda.usuarios.controller;

import com.tp_bda.usuarios.dto.ClienteDTO;
import com.tp_bda.usuarios.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);

    /* Crea un nuevo perfil de cliente */
    @PostMapping
    public ResponseEntity<?> crearCliente(@RequestBody ClienteDTO clienteDTO) {
        try {
            ClienteDTO nuevo = clienteService.guardar(clienteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos al crear cliente: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error creando cliente", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    /* Busca un cliente por su ID de BBDD */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            ClienteDTO dto = clienteService.buscarPorId(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error obteniendo cliente {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    /* Busca el perfil del cliente que está actualmente autenticado */
    @GetMapping("/mi-perfil")
    public ResponseEntity<?> obtenerMiPerfil(@AuthenticationPrincipal Jwt jwt) {
        try {
            String keycloakUserId = jwt.getSubject();
            ClienteDTO dto = clienteService.buscarPorKeycloakId(keycloakUserId);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error obteniendo perfil del cliente autenticado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    /* Actualiza el perfil de un cliente */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable Integer id, @RequestBody ClienteDTO dtoActualizado) {
        try {
            ClienteDTO actualizado = clienteService.actualizar(id, dtoActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException e) {
            log.warn("Datos inválidos al actualizar cliente {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            log.error("Error actualizando cliente {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    /* Lista todos los perfiles de clientes */
    @GetMapping
    public ResponseEntity<?> listarClientes() {
        try {
            List<ClienteDTO> list = clienteService.listar();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("Error listando clientes", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }
}
