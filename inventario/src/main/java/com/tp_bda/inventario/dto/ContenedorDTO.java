package com.tp_bda.inventario.dto;

import com.tp_bda.inventario.model.EstadoContenedor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ContenedorDTO {

    private Integer id;

    @NotNull(message = "El peso es requerido")
    @Positive(message = "El peso debe ser positivo")
    private BigDecimal peso;

    @NotNull(message = "El volumen es requerido")
    @Positive(message = "El volumen debe ser positivo")
    private BigDecimal volumen;

    @NotNull(message = "El estado es requerido")
    private EstadoContenedor estado; // Usamos el Enum directamente

    @NotNull(message = "El ID de cliente es requerido")
    private Integer clienteId;

    // El ID del depósito donde se encuentra.
    // Puede ser nulo si, por ejemplo, está EN_TRANSITO.
    private Integer depositoId;

    // --- Getters y Setters (Estilo manual) ---

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPeso() {
        return peso;
    }
    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getVolumen() {
        return volumen;
    }
    public void setVolumen(BigDecimal volumen) {
        this.volumen = volumen;
    }

    public EstadoContenedor getEstado() {
        return estado;
    }
    public void setEstado(EstadoContenedor estado) {
        this.estado = estado;
    }

    public Integer getClienteId() {
        return clienteId;
    }
    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public Integer getDepositoId() {
        return depositoId;
    }
    public void setDepositoId(Integer depositoId) {
        this.depositoId = depositoId;
    }
}