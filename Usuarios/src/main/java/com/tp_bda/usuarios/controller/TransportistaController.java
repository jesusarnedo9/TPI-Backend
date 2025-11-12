package com.tp_bda.usuarios.controller;

import com.tp_bda.usuarios.dto.TransportistaDTO;
import com.tp_bda.usuarios.service.TransportistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transportistas")
public class TransportistaController {

    @Autowired
    private TransportistaService transportistaService;

    // Crear un nuevo transportista
    @PostMapping
    public ResponseEntity<TransportistaDTO> crear(@RequestBody TransportistaDTO transportistaDTO) {
        TransportistaDTO nuevo = transportistaService.guardar(transportistaDTO);
        return ResponseEntity.ok(nuevo);
    }

    // Obtener transportista por ID
    @GetMapping("/{id}")
    public ResponseEntity<TransportistaDTO> obtenerPorId(@PathVariable Integer id) {
        TransportistaDTO dto = transportistaService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    // Consultar el perfil del transportista autenticado (Mejor práctica de seguridad)
    // Permite al transportista ver su perfil usando su token, sin pasar el ID.
    @PreAuthorize("hasAuthority('TRANSPORTISTA')")
    @GetMapping("/mi-perfil")
    public ResponseEntity<TransportistaDTO> obtenerMiPerfil(@AuthenticationPrincipal Jwt jwt) {
        // Extraemos el ID del usuario de Keycloak desde el token (el 'sub')
        String keycloakUserId = jwt.getSubject();
        TransportistaDTO dto = transportistaService.buscarPorKeycloakId(keycloakUserId);
        return ResponseEntity.ok(dto);
    }

    // Actualizar transportista
    @PutMapping("/{id}")
    public ResponseEntity<TransportistaDTO> actualizar(@PathVariable Integer id, @RequestBody TransportistaDTO transportistaDTO) {
        TransportistaDTO actualizado = transportistaService.actualizar(id, transportistaDTO);
        return ResponseEntity.ok(actualizado);
    }

    // Listar todos los transportistas
    @GetMapping
    public List<TransportistaDTO> listar() {
        return transportistaService.listar();
    }
}
