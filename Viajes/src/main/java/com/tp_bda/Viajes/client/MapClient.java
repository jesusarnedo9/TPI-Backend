package com.tp_bda.Viajes.client;

import org.springframework.stereotype.Component;

@Component
public class MapClient {

    // Radio de la Tierra en Kilómetros (km), necesario para la fórmula de Haversine
    private static final int RADIO_TIERRA_KM = 6371;

    /**
     * Calcula la distancia entre dos puntos geográficos (Lat/Lon) usando la fórmula de Haversine.
     * Este método simula la funcionalidad de una API de mapas (como Google Maps o OSRM).
     * * @param lat1 Latitud de Origen.
     * @param lon1 Longitud de Origen.
     * @param lat2 Latitud de Destino.
     * @param lon2 Longitud de Destino.
     * @return Distancia calculada en Kilómetros (Double).
     */
    public Double getDistancia(double lat1, double lon1, double lat2, double lon2) {

        // --- 1. Conversión a Radianes ---
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);

        // --- 2. Fórmula de Haversine ---
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(rLat1) * Math.cos(rLat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distanciaKm = RADIO_TIERRA_KM * c;

        // Si la distancia es muy corta (cerca de 0), devolvemos un valor base para simular una ruta local.
        if (distanciaKm < 1.0) {
            return 10.0;
        }

        // Devolvemos la distancia redondeada a dos decimales
        return Math.round(distanciaKm * 100.0) / 100.0;
    }
}