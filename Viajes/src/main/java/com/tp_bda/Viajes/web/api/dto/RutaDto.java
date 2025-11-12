package com.tp_bda.Viajes.web.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RutaDto {

    private Integer id;

    private Integer solicitudId;

    private Integer cantidadTramos;

    private Integer cantidadDepositos;

    // Contiene la lista de segmentos (Tramos) de esta ruta
    private List<TramoDto> tramos;
}