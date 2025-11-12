package com.tp_bda.Viajes.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono; // Importación necesaria para Mono

import java.util.Optional;

@Component
public class InventarioClient {

    private final WebClient webClient;

    public InventarioClient(@Qualifier("inventarioWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Obtiene el token JWT actual del contexto de seguridad.
     * @return El token JWT como String (Bearer Token).
     */
    private Optional<String> getCurrentJwt() {
        // Accede al token JWT (Credentials) que Spring Security validó
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(auth -> (Jwt) auth.getCredentials())
                .map(Jwt::getTokenValue);
    }


    /**
     * Valida si un camión con ese ID existe y cumple con los límites de capacidad.
     * Endpoint esperado en Inventario: GET /api/inventario/camiones/{camionId}/validar?peso={p}&volumen={v}
     */
    public boolean validarCapacidadCamion(Integer camionId, Double pesoContenedor, Double volumenContenedor) {
        String token = getCurrentJwt()
                .orElseThrow(() -> new IllegalStateException("JWT no disponible para llamada a Inventario. Asegúrese de que la petición esté autenticada."));

        String path = String.format("/api/inventario/camiones/%d/validar?peso=%f&volumen=%f",
                camionId, pesoContenedor, volumenContenedor);

        try {
            return webClient.get()
                    .uri(path)
                    .header("Authorization", "Bearer " + token) // Adjunta el token para la autenticación
                    .retrieve()

                    // Manejo de status 4xx (Errores de cliente, ej. Validación fallida, Camión no apto)
                    .onStatus(status -> status.is4xxClientError(), clientResponse -> {
                        // Si Inventario devuelve 4xx (ej. 409 Conflict), lo tratamos como "No apto" (false).
                        // Devolvemos un Mono vacío, lo que hace que blockOptional() retorne Optional.empty().
                        System.err.println("Validación de Inventario falló con status: " + clientResponse.statusCode());
                        return Mono.empty();
                    })

                    // Manejo de status 5xx (Errores de servidor)
                    .onStatus(status -> status.is5xxServerError(), clientResponse -> {
                        // Para errores graves, lanzamos una excepción de WebClient
                        return clientResponse.createException().flatMap(Mono::error);
                    })

                    // Espera un Boolean (true si pasa la validación)
                    .bodyToMono(Boolean.class)

                    // Bloquea y espera el resultado. Si Mono está vacío (por error 4xx), devuelve false.
                    .blockOptional()
                    .orElse(false);

        } catch (Exception e) {
            // Captura errores de conexión o excepciones lanzadas por createException (5xx)
            System.err.println("Error fatal de comunicación con Inventario: " + e.getMessage());
            return false;
        }
    }
}