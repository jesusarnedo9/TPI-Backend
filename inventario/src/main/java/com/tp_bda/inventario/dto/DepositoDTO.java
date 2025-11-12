package com.tp_bda.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class DepositoDTO {

    private Integer id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String direccion;

    @NotNull(message = "La latitud es requerida")
    private BigDecimal latitud;

    @NotNull(message = "La longitud es requerida")
    private BigDecimal longitud;

    @NotNull(message = "El costo de estadía es requerido")
    @Positive(message = "El costo de estadía debe ser positivo")
    private BigDecimal costoEstadiaDiaria;

    // Constructor vacío
    public DepositoDTO() {
    }

    // --- Getters y Setters (Estilo manual) ---

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }
    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }
    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public BigDecimal getCostoEstadiaDiaria() {
        return costoEstadiaDiaria;
    }
    public void setCostoEstadiaDiaria(BigDecimal costoEstadiaDiaria) {
        this.costoEstadiaDiaria = costoEstadiaDiaria;
    }
}