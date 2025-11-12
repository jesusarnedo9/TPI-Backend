package com.tp_bda.usuarios.repository;

import com.tp_bda.usuarios.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    /* Busca el perfil de Cliente usando el ID (UUID) que Keycloak
     * emite en el JWT. Spring Data JPA crea la consulta automáticamente.
     */
    Optional<Cliente> findByKeycloakUserId(String keycloakUserId);
}

