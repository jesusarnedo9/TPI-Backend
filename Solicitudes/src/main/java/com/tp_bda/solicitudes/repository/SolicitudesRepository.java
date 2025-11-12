package com.tp_bda.solicitudes.repository;

import com.tp_bda.solicitudes.model.Solicitudes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Importar List

@Repository
public interface SolicitudesRepository extends JpaRepository<Solicitudes, Integer> {

    /**
     * Spring Data JPA crea automáticamente la consulta SQL
     * (SELECT * FROM solicitudes WHERE cliente_id = ?)
     * solo por el nombre del método.
     */
    List<Solicitudes> findByClienteId(Integer clienteId);
}