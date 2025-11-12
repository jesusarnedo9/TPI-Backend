package com.tp_bda.inventario.repository;

import com.tp_bda.inventario.model.Contenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Buena práctica
public interface ContenedorRepository extends JpaRepository<Contenedor, Integer> {
    // JpaRepository ya nos da todo el CRUD básico
}