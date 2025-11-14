package com.tp_bda.Viajes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${api.inventario.url}")
    private String inventarioBaseUrl;

    @Value("${api.tarifas.url}")
    private String tarifasBaseUrl;

    // URL base de la API Externa de Mapas (Configurada en application.properties de Viajes)
    @Value("${api.maps.base-url}")
    private String mapsBaseUrl;

    /**
     * Cliente para el microservicio de Inventario (para validar camiones)
     */
    @Bean("inventarioWebClient")
    public WebClient inventarioWebClient() {
        return WebClient.builder()
                .baseUrl(inventarioBaseUrl)
                .build();
    }
    /**
     * Cliente para el microservicio de Tarifas (para calcular costos estimados/reales)
     */
    @Bean("tarifasWebClient")
    public WebClient tarifasWebClient() {
        return WebClient.builder()
                .baseUrl(tarifasBaseUrl)
                .build();
    }

    /**
     * Cliente para la API externa de Mapas (para calcular distancias)
     */
    @Bean("mapsWebClient")
    public WebClient mapsWebClient() {
        return WebClient.builder()
                .baseUrl(mapsBaseUrl)
                .build();
    }
}