package com.tp_bda.usuarios.dto;

public class TransportistaDTO {

    private Integer id;

    // Campo clave para la integración con Keycloak.
    private String keycloakUserId;

    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;

    public TransportistaDTO() {}

    // Constructor actualizado
    public TransportistaDTO(Integer id, String keycloakUserId, String nombre, String apellido, String dni, String telefono) {
        this.id = id;
        this.keycloakUserId = keycloakUserId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
    }

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