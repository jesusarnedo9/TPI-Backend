package com.tp_bda.Viajes.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;

@Component
public class TarifasClient {

    private final WebClient webClient;

    public TarifasClient(@Qualifier("tarifasWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    private Optional<String> getCurrentJwt() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(auth -> (Jwt) auth.getCredentials())
                .map(Jwt::getTokenValue);
    }

    /**
     * Llama al microservicio Tarifas para obtener el costo estimado de la ruta.
     * Endpoint esperado en Tarifas: POST /api/tarifas/aproximada
     * @return Map con "costoEstimado" y "tiempoEstimado"
     */
    public Map<String, Double> calcularTarifaEstimada(Double distanciaKm, Double pesoContenedor) {
        String token = getCurrentJwt()
                .orElseThrow(() -> new IllegalStateException("JWT no disponible para llamada a Tarifas."));

        // El MS Tarifas de tu equipo debe implementar este endpoint.
        try {
            return webClient.post()
                    .uri("/api/tarifas/aproximada")
                    .header("Authorization", "Bearer " + token)
                    .bodyValue(Map.of("distanciaKm", distanciaKm, "pesoContenedor", pesoContenedor))
                    .retrieve()
                    .bodyToMono(new org.springframework.core.ParameterizedTypeReference<Map<String, Double>>() {})
                    .block();

        } catch (Exception e) {
            // Si el servicio de tarifas está caído o la llamada falla, devolvemos un estimado local (simulación)
            System.err.println("Advertencia: Falló la comunicación con Tarifas. Usando estimado local.");
            return Map.of(
                    "costoEstimado", distanciaKm * 1.5 + (pesoContenedor * 0.1),
                    "tiempoEstimadoHoras", distanciaKm / 80.0 // 80 km/h velocidad promedio
            );
        }
    }
}