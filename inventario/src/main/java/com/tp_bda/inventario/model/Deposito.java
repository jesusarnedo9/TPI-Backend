package com.tp_bda.inventario.model;

import jakarta.persistence.*;
import java.math.BigDecimal; // Importado para precisión
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "deposito")
public class Deposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deposito_id")
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private BigDecimal latitud; // Usamos BigDecimal para precisión

    @Column(nullable = false)
    private BigDecimal longitud; // Usamos BigDecimal para precisión

    @Column(nullable = false)
    private BigDecimal costoEstadiaDiaria; // Usamos BigDecimal para dinero

    // Relación 1 a N con Contenedor
    // Quitado "cascade = CascadeType.ALL" para evitar borrados en cascada
    @OneToMany(mappedBy = "deposito", fetch = FetchType.LAZY)
    private List<Contenedor> contenedores = new ArrayList<>();

    // Constructor vacío requerido por JPA
    public Deposito() {
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

    public List<Contenedor> getContenedores() {
        return contenedores;
    }
    public void setContenedores(List<Contenedor> contenedores) {
        this.contenedores = contenedores;
    }
}