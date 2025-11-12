package com.tp_bda.inventario.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal; // Importante: Usar BigDecimal para precisión

@Entity
@Table(name = "camion")
@Data // Genera Getters, Setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "camion_id")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String dominio;

    @Column(nullable = false)
    private BigDecimal capacidadPeso; // Kilos, por ejemplo

    @Column(nullable = false)
    private BigDecimal capacidadVolumen; // Metros cúbicos, por ejemplo

    @Column(nullable = false)
    private BigDecimal costos; // Costo por km o por hora

    @Column(nullable = false)
    private Boolean disponible;

    @Column(name = "transportista_id", nullable = false)
    private Integer transportistaId;

}