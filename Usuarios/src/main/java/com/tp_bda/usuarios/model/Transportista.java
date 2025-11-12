package com.tp_bda.usuarios.model;

import jakarta.persistence.*;

// Importamos solo lo necesario (eliminamos java.util.List)

@Entity
@Table(name = "transportista")
public class Transportista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transportista_id")
    private Integer id;

    // Campo de integración clave: ID único del usuario en Keycloak (el 'sub' del JWT)
    @Column(name = "keycloak_user_id", unique = true, nullable = false)
    private String keycloakUserId; // Guardará el UUID del usuario autenticado

    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    // Getter y Setter para el nuevo campo de Keycloak
    public String getKeycloakUserId() { return keycloakUserId; }
    public void setKeycloakUserId(String keycloakUserId) { this.keycloakUserId = keycloakUserId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}