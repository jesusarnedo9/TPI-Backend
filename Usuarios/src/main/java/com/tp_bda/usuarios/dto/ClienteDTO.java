package com.tp_bda.usuarios.dto;

public class ClienteDTO {

    private Integer id;

    // Campo clave para la integración con Keycloak
    // Representa el ID único del usuario autenticado (UUID)
    private String keycloakUserId;

    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private String direccion;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    // Nuevo Getter y Setter para el campo de Keycloak
    public String getKeycloakUserId() { return keycloakUserId; }
    public void setKeycloakUserId(String keycloakUserId) { this.keycloakUserId = keycloakUserId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}