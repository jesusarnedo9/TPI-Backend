package com.tp_bda.solicitudes.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SolicitudResponseDTO {

    private Integer solicitudId;
    private String estado; // Ej: "PENDIENTE"
    private LocalDateTime fechaHoraSalida;
    private Integer clienteId;
    private Integer contenedorId; // ID devuelto por MS Inventario
    private Integer rutaId;       // ID devuelto por MS Viajes
    private Double costoAproximado; // Costo devuelto por MS Tarifas
}