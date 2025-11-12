package com.tp_bda.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data // Genera Getters, Setters, etc.
public class CamionDTO {

    private Integer id;

    @NotBlank(message = "El dominio no puede estar vacío")
    private String dominio;

    @NotNull(message = "La capacidad de peso es requerida")
    @Positive(message = "La capacidad de peso debe ser positiva")
    private BigDecimal capacidadPeso;

    @NotNull(message = "La capacidad de volumen es requerida")
    @Positive(message = "La capacidad de volumen debe ser positiva")
    private BigDecimal capacidadVolumen;

    @NotNull(message = "El costo es requerido")
    @Positive(message = "El costo debe ser positivo")
    private BigDecimal costos;

    @NotNull(message = "La disponibilidad es requerida (true/false)")
    private Boolean disponible;

    @NotNull(message = "El ID de transportista es requerido")
    private Integer transportistaId;
}