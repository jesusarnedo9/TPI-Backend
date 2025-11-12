package com.tp_bda.tarifas.repository;

import com.tp_bda.tarifas.model.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {

    // Podríamos usar esto para buscar tarifas base, ej:
    Optional<Tarifa> findByTipo(String tipo);
}