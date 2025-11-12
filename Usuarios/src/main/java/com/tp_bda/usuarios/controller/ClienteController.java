package com.tp_bda.usuarios.controller;

import com.tp_bda.usuarios.dto.ClienteDTO;
import com.tp_bda.usuarios.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /* Crea un nuevo perfil de cliente */
    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@RequestBody ClienteDTO clienteDTO) {
        ClienteDTO nuevo = clienteService.guardar(clienteDTO);
        return ResponseEntity.ok(nuevo);
    }

    /* Busca un cliente por su ID de BBDD */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerPorId(@PathVariable Integer id) {
        ClienteDTO dto = clienteService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    /* Busca el perfil del cliente que está actualmente autenticado */
    @GetMapping("/mi-perfil")
    public ResponseEntity<ClienteDTO> obtenerMiPerfil(@AuthenticationPrincipal Jwt jwt) {
        // Obtenemos el ID de usuario ('sub') del token JWT
        String keycloakUserId = jwt.getSubject();

        // Usamos el método que creamos en el servicio
        ClienteDTO dto = clienteService.buscarPorKeycloakId(keycloakUserId);
        return ResponseEntity.ok(dto);
    }

    /* Actualiza el perfil de un cliente */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(@PathVariable Integer id, @RequestBody ClienteDTO dtoActualizado) {
        ClienteDTO actualizado = clienteService.actualizar(id, dtoActualizado);
        return ResponseEntity.ok(actualizado);
    }

    /* Lista todos los perfiles de clientes */
    @GetMapping
    public List<ClienteDTO> listarClientes() {
        return clienteService.listar();
    }
}
