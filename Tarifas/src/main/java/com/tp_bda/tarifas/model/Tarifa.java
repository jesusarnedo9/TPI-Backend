package com.tp_bda.tarifas.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "tarifas")
@Data // Genera getters, setters, toString, etc. (Requiere Lombok)
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tarifa_id")
    private Long id; // PK tarifa_id int

    @Column(length = 50)
    private String tipo; // tipo varchar

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valor; // valor decimal (Usamos BigDecimal para precisión monetaria)

    @Column(length = 255)
    private String descripcion; // descripcion varchar

    // --- Relaciones a otros Microservicios (Foreign Keys) ---

    // FK a Solicitudes MS
    @Column(name = "solicitud_id")
    private Long solicitudId; // solicitud_id int

    // FK a Inventario MS
    @Column(name = "camion_id")
    private Long camionId; // camion_id int

    // FK a Viajes MS
    @Column(name = "tramo_id")
    private Long tramoId; // tramo_id int
}