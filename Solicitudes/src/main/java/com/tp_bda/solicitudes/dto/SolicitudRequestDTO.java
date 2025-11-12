package com.tp_bda.solicitudes.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SolicitudRequestDTO {

    // Datos para el Contenedor (MS Inventario)
    private double peso;
    private double volumen;
    private String descripcionCarga;

    // Datos para la Ruta (MS Viajes)
    private String direccionOrigen;
    private String direccionDestino;

    // Datos de la Solicitud
    private LocalDateTime fechaHoraSalida;

    // NOTA: El 'clienteId' no se pide aquí, se obtendrá
    // del token de seguridad (JWT) del usuario logueado.
}