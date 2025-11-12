package com.tp_bda.Viajes.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime; // Se recomienda usar LocalDateTime para campos DATETIME/TIMESTAMP
import java.util.Objects;

@Entity
@Table(name = "Tramos")
@Getter @Setter @NoArgsConstructor
public class Tramos {

    /** PK: Identificador único del tramo. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // PK tramo id int

    /** Origen del tramo (Dirección o Depósito). */
    @Basic
    private String origen; // origen varchar

    /** Destino del tramo (Dirección o Depósito). */
    @Basic
    private String destino; // destino varchar

    /** Tipo de tramo (origen-deposito, deposito-deposito, deposito-destino, origen-destino). */
    @Basic
    private String tipo; // tipo varchar

    /** Estado actual del tramo (estimado, asignado, iniciado, finalizado). */
    @Enumerated(EnumType.STRING) // Guarda el valor del enum como String en la DB
    private EstadoTramo estado; // estado enum

    /** Costo estimado antes de la realización del viaje. */
    @Column(name = "costo_aproximado", columnDefinition = "DECIMAL(10, 2)")
    private Double costoAproximado; // costoAproximado decimal

    /** Costo final real del tramo. */
    @Column(name = "costo_real", columnDefinition = "DECIMAL(10, 2)")
    private Double costoReal; // costoReal decimal

    /** Fecha y hora real de inicio del tramo. */
    @Column(name = "fecha_hora_inicio")
    private LocalDateTime fechaHoraInicio; // fecha Horalnicio datetime

    /** Fecha y hora real de finalización del tramo. */
    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin; // fechaHoraFin datetime

    /** ID del camión asignado (FK a Microservicio Inventario). */
    @Column(name = "camion_id")
    private Integer camionId; // camion id int

    /** Relación Many-to-One con la Ruta a la que pertenece este tramo. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruta_id")
    private Rutas ruta; // FK ruta id int

    // (Opcional: puedes añadir un equals y hashCode basado en el id si es necesario)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tramos that = (Tramos) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}