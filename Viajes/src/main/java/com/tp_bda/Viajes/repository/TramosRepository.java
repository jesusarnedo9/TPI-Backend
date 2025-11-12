package com.tp_bda.Viajes.repository;

import com.tp_bda.Viajes.models.Tramos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TramosRepository extends JpaRepository<Tramos, Integer> {
    // Buscar tramos asignados a un camión específico para el Transportista
    // Necesario para el endpoint /rutas/asignadas/transportista/{id}
    List<Tramos> findByCamionId(Integer camionId);

    // Buscar tramos por su estado (ej. para gestión de rutas en tránsito)
    // Se asume que EstadoTramo es un Enum o String en la base de datos
    // List<Tramos> findByEstado(String estado);
}