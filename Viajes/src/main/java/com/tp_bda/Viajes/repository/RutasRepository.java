package com.tp_bda.Viajes.repository;

import com.tp_bda.Viajes.models.Rutas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutasRepository extends JpaRepository<Rutas, Integer> {
    // Aquí puedes añadir consultas personalizadas si las necesitas.
}