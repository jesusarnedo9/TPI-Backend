package com.tp_bda.solicitudes.controller;

import com.tp_bda.solicitudes.dto.SolicitudRequestDTO;
import com.tp_bda.solicitudes.dto.SolicitudResponseDTO;
import com.tp_bda.solicitudes.service.SolicitudesService;
import com.tp_bda.solicitudes.client.InventarioClient;
import com.tp_bda.solicitudes.client.TarifasClient;
import com.tp_bda.solicitudes.client.ViajesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List; // Importar List
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudesController {

    @Autowired
    private SolicitudesService solicitudesService;

    private static final Logger log = LoggerFactory.getLogger(SolicitudesController.class);

    /**
     * Endpoint para que un Cliente cree una nueva solicitud (POST).
     */
    @PostMapping
    public ResponseEntity<?> crearNuevaSolicitud(
            @RequestBody SolicitudRequestDTO request,
            @AuthenticationPrincipal Jwt jwt // Inyecta el token JWT
    ) {
        try {
            SolicitudResponseDTO response = solicitudesService.crearSolicitud(request, jwt);
            // Created
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            log.warn("Solicitud inválida: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));

        } catch (IllegalStateException e) {
            log.warn("Estado no permitido al crear solicitud: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));

        } catch (Exception e) {
            log.error("Error creando solicitud", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error interno al crear la solicitud"));
        }
    }

    // --- MÉTODO NUEVO PARA CONSULTAR (GET) ---

    /**
     * Endpoint para que un Cliente consulte SUS solicitudes (GET).
     */
    @GetMapping
    public ResponseEntity<?> getSolicitudesDelCliente(
            @AuthenticationPrincipal Jwt jwt // Inyecta el token JWT
    ) {
        try {
            // Saca el ID del cliente del mismo token
            String clienteIdStr = jwt.getClaimAsString("clienteId");
            if (clienteIdStr == null || clienteIdStr.isBlank()) {
                log.warn("Claim 'clienteId' ausente en token");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Claim 'clienteId' ausente en token"));
            }

            Integer clienteId;
            try {
                clienteId = Integer.parseInt(clienteIdStr);
            } catch (NumberFormatException nfe) {
                log.warn("Claim 'clienteId' con formato inválido: {}", clienteIdStr);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Claim 'clienteId' con formato inválido: " + clienteIdStr));
            }

            List<SolicitudResponseDTO> dtos = solicitudesService.consultarPorClienteId(clienteId);
            return ResponseEntity.ok(dtos);

        } catch (RuntimeException e) {
            log.error("Error consultando solicitudes para cliente", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error interno al consultar solicitudes"));
        }
    }
}