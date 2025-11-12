package com.tp_bda.solicitudes.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TarifasClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.tarifas.url}") // Lee la URL de application.properties
    private String tarifasUrl;

    /**
     * Llama al MS Tarifas para obtener la estimación.
     * Coincide con: GET /api/tarifas/estimar?distancia=...&peso=...&volumen=...
     */
    public Double estimarTarifa(Double distanciaKm, Double peso, Double volumen) {

        String tarifaUrlEstimada = String.format(
                "%s/api/tarifas/estimar?distancia=%.2f&peso=%.2f&volumen=%.2f",
                tarifasUrl, distanciaKm, peso, volumen
        );

        try {
            // 4. Llamar a MS Tarifas
            return restTemplate.getForObject(tarifaUrlEstimada, Double.class);
        } catch (Exception e) {
            System.err.println("Error al llamar a MS Tarifas: " + e.getMessage());
            // En un caso real, podrías lanzar una excepción personalizada
            return null;
        }
    }
}