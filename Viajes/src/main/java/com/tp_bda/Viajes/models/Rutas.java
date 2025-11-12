package com.tp_bda.Viajes.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Rutas")
@Getter @Setter @NoArgsConstructor
public class Rutas {

    /** PK: Identificador único de la ruta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** ID de la Solicitud asociada (FK a Microservicio Solicitudes). */
    @Column(name = "solicitud_id", nullable = false)
    private Integer solicitudId;

    /** Cantidad total de tramos que componen la ruta. */
    @Column(name = "cantidad_tramos")
    private Integer cantidadTramos; // campo 'cantidad Tramos'

    /** Cantidad de depósitos intermedios en la ruta. */
    @Column(name = "cantidad_depositos")
    private Integer cantidadDepositos; // campo 'cantidad Depositos'

    /** Relación One-to-Many con los tramos de esta ruta. */
    @OneToMany(mappedBy = "ruta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tramos> tramos;

    // (Opcional: puedes añadir un equals y hashCode basado en el id si es necesario)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rutas that = (Rutas) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}