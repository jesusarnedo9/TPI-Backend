package com.tp_bda.inventario.repository;

import com.tp_bda.inventario.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Buena práctica agregar la anotación
public interface CamionRepository extends JpaRepository<Camion, Integer> {
    // Aquí podrías agregar búsquedas personalizadas si las necesitas,
    // por ejemplo: Optional<Camion> findByDominio(String dominio);
}