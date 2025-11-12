package com.tp_bda.solicitudes.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class ViajesClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.viajes.url}")
    private String viajesUrl;

    /**
     * Llama al MS Viajes para crear una ruta tentativa.
     * Coincide con: POST /api/viajes/rutas/tentativas
     * Devuelve el Map de respuesta (que contiene "id" y "distanciaKm").
     */
    public Map<String, Object> crearRutaTentativa(String origen, String destino) {

        Map<String, Object> rutaRequest = Map.of(
                "origen", origen,
                "destino", destino
        );

        try {
            // 3. Llamar a MS Viajes
            return restTemplate.postForObject(
                    viajesUrl + "/api/viajes/rutas/tentativas",
                    rutaRequest,
                    Map.class
            );
        } catch (Exception e) {
            System.err.println("Error al llamar a MS Viajes: " + e.getMessage());
            return null;
        }
    }
}
