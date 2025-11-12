package com.tp_bda.inventario.model;

import jakarta.persistence.*;
import java.math.BigDecimal; // Importante para precisión

@Entity
@Table(name = "contenedor")
public class Contenedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contenedor_id")
    private Integer id;

    @Column(nullable = false)
    private BigDecimal peso; // En Kilos

    @Column(nullable = false)
    private BigDecimal volumen; // En Metros Cúbicos

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoContenedor estado;

    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId; // ID "débil" del microservicio de Usuarios

    // Relación N a 1 con Deposito
    @ManyToOne(fetch = FetchType.LAZY) // LAZY es mejor para rendimiento
    @JoinColumn(name = "deposito_id") // Puede ser nulo si está en tránsito o entregado
    private Deposito deposito;

    // Constructor vacío requerido por JPA
    public Contenedor() {
    }

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

    public Deposito getDeposito() {
        return deposito;
    }
    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }
}