package com.tp_bda.Viajes.web.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutaTentativaDto {

    private String origen;
    private String destino;
    private Double distanciaKm;
    private Double tiempoEstimadoHoras;
    private Double costoEstimado;

    // Aquí se podría añadir una lista de tramos sugeridos (opcional)
}