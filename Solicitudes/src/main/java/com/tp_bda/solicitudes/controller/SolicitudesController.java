package com.tp_bda.solicitudes.controller;

import com.tp_bda.solicitudes.dto.SolicitudRequestDTO;
import com.tp_bda.solicitudes.dto.SolicitudResponseDTO;
import com.tp_bda.solicitudes.service.SolicitudesService;
import com.tp_bda.solicitudes.client.InventarioClient;
import com.tp_bda.solicitudes.client.TarifasClient;
import com.tp_bda.solicitudes.client.ViajesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Importar List

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudesController {

    @Autowired
    private SolicitudesService solicitudesService;

    /**
     * Endpoint para que un Cliente cree una nueva solicitud (POST).
     */
    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> crearNuevaSolicitud(
            @RequestBody SolicitudRequestDTO request,
            @AuthenticationPrincipal Jwt jwt // Inyecta el token JWT
    ) {
        try {
            SolicitudResponseDTO response = solicitudesService.crearSolicitud(request, jwt);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Manejo básico de errores
            return ResponseEntity.status(500).body(null);
        }
    }

    // --- MÉTODO NUEVO PARA CONSULTAR (GET) ---

    /**
     * Endpoint para que un Cliente consulte SUS solicitudes (GET).
     */
    @GetMapping
    public ResponseEntity<List<SolicitudResponseDTO>> getSolicitudesDelCliente(
            @AuthenticationPrincipal Jwt jwt // Inyecta el token JWT
    ) {
        try {
            // Saca el ID del cliente del mismo token
            // ¡RECUERDA VERIFICAR EL NOMBRE DEL CLAIM!
            Integer clienteId = Integer.parseInt(jwt.getClaimAsString("clienteId"));

            List<SolicitudResponseDTO> dtos = solicitudesService.consultarPorClienteId(clienteId);
            return ResponseEntity.ok(dtos);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}