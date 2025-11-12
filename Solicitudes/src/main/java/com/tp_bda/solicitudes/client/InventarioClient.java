package com.tp_bda.solicitudes.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class InventarioClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service.inventario.url}")
    private String inventarioUrl;

    /**
     * Llama al MS Inventario para crear un contenedor.
     * Coincide con: POST /api/inventario/contenedores
     * Devuelve el ID del contenedor creado.
     */
    public Integer crearContenedor(Double peso, Double volumen, String descripcion) {

        Map<String, Object> contenedorRequest = Map.of(
                "peso", peso,
                "volumen", volumen,
                "descripcion", descripcion
        );

        try {
            // 2. Llamar a MS Inventario
            Map<String, Object> contenedorResponse = restTemplate.postForObject(
                    inventarioUrl + "/api/inventario/contenedores",
                    contenedorRequest,
                    Map.class
            );

            if (contenedorResponse != null && contenedorResponse.containsKey("id")) {
                return (Integer) contenedorResponse.get("id");
            }
            throw new RuntimeException("Respuesta de Inventario no válida, no contiene ID.");

        } catch (Exception e) {
            System.err.println("Error al llamar a MS Inventario: " + e.getMessage());
            return null;
        }
    }
}