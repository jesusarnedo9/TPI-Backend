package com.tp_bda.Viajes.web.api.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data // (o @Getter y @Setter)
public class TramoDto {
    private Integer id;
    private String origen;
    private String destino;
    private String tipo;
    private String estado;
    private Double costoAproximado;
    private Double costoReal;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Integer camionId;
    private Integer rutaId;
}