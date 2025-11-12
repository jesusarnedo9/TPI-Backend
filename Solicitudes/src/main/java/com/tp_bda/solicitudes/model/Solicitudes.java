package com.tp_bda.solicitudes.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes")
@Data // Genera getters, setters, toString, etc. (Requiere Lombok)
public class Solicitudes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solicitud_id")
    private Integer id; // PK solicitud_id int

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud = LocalDateTime.now(); // fechaSolicitud datetime

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estado; // estado enum

    @Column(name = "costo_estimado", precision = 10, scale = 2)
    private BigDecimal costoEstimado; // costoEstimado decimal

    @Column(name = "tiempo_estimado", precision = 10, scale = 2)
    private BigDecimal tiempoEstimado; // tiempoEstimado decimal

    @Column(name = "costo_final", precision = 10, scale = 2)
    private BigDecimal costoFinal; // costoFinal decimal

    @Column(name = "tiempo_real", precision = 10, scale = 2)
    private BigDecimal tiempoReal; // tiempoReal decimal

    @Column(name = "fecha_hora_salida")
    private LocalDateTime fechaHoraSalida;

    // --- Relaciones a otros Microservicios (Foreign Keys) ---

    // FK a Inventario MS (Contenedor)
    @Column(name = "contenedor_id", nullable = false)
    private Integer contenedorId; // contenedor_id int

    // FK a Usuarios MS (Cliente)
    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId; // cliente_id int

    // FK a Viajes MS (Ruta)
    @Column(name = "ruta_id")
    private Integer rutaId; // ruta_id int
}
