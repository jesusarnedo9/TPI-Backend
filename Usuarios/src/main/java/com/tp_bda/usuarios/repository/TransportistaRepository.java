package com.tp_bda.usuarios.repository;

import com.tp_bda.usuarios.model.Transportista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportistaRepository extends JpaRepository<Transportista, Integer> {


    // Busca el perfil de Transportista usando el ID (UUID) que Keycloak emite en el JWT.
    Optional<Transportista> findByKeycloakUserId(String keycloakUserId);
}