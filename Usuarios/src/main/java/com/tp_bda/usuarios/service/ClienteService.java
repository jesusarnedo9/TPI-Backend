package com.tp_bda.usuarios.service;

import com.tp_bda.usuarios.dto.ClienteDTO;
import com.tp_bda.usuarios.model.Cliente;
import com.tp_bda.usuarios.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    /* Guarda un nuevo perfil de cliente. */
    public ClienteDTO guardar(ClienteDTO dto) {
        Cliente c = fromDto(dto); // Mapeador DTO -> Entidad
        Cliente guardado = clienteRepository.save(c);
        return toDto(guardado); // Mapeador Entidad -> DTO
    }

    /* Busca un cliente por su ID de base de datos */
    public ClienteDTO buscarPorId(Integer id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        return toDto(c);
    }

    /* Busca un perfil de cliente usando el ID de Keycloak (el 'sub' del JWT).
     * Usado por el endpoint /mi-perfil.
     */
    public ClienteDTO buscarPorKeycloakId(String keycloakUserId) {
        Cliente c = clienteRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new RuntimeException("Perfil de Cliente no encontrado para el usuario Keycloak ID: " + keycloakUserId));
        return toDto(c);
    }

    /* Actualiza un perfil de cliente existente. */
    public ClienteDTO actualizar(Integer id, ClienteDTO dtoActualizado) {
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado para actualizar con ID: " + id));

        // Actualiza solo los campos que vienen en el DTO
        if (dtoActualizado.getNombre() != null) existente.setNombre(dtoActualizado.getNombre());
        if (dtoActualizado.getApellido() != null) existente.setApellido(dtoActualizado.getApellido());
        if (dtoActualizado.getDni() != null) existente.setDni(dtoActualizado.getDni());
        if (dtoActualizado.getEmail() != null) existente.setEmail(dtoActualizado.getEmail());
        if (dtoActualizado.getTelefono() != null) existente.setTelefono(dtoActualizado.getTelefono());
        if (dtoActualizado.getDireccion() != null) existente.setDireccion(dtoActualizado.getDireccion());
        // No permitimos actualizar el keycloakUserId

        Cliente actualizado = clienteRepository.save(existente);
        return toDto(actualizado);
    }

    /* Lista todos los clientes. */
    public List<ClienteDTO> listar() {
        return clienteRepository.findAll()
                .stream()
                .map(this::toDto) // Usa el mapeador toDto
                .collect(Collectors.toList());
    }

    // ------------------------------------------------------------------
    // Mapeadores Privados (Convertidores)
    // ------------------------------------------------------------------

    /* Convierte una Entidad (Cliente) a un DTO (ClienteDTO). */
    private ClienteDTO toDto(Cliente c) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(c.getId());
        dto.setKeycloakUserId(c.getKeycloakUserId());
        dto.setNombre(c.getNombre());
        dto.setApellido(c.getApellido());
        dto.setDni(c.getDni());
        dto.setEmail(c.getEmail());
        dto.setTelefono(c.getTelefono());
        dto.setDireccion(c.getDireccion());
        return dto;
    }

    /* Convierte un DTO (ClienteDTO) a una Entidad (Cliente). */
    private Cliente fromDto(ClienteDTO dto) {
        Cliente c = new Cliente();
        //c.setId(dto.getId());
        c.setKeycloakUserId(dto.getKeycloakUserId());
        c.setNombre(dto.getNombre());
        c.setApellido(dto.getApellido());
        c.setDni(dto.getDni());
        c.setEmail(dto.getEmail());
        c.setTelefono(dto.getTelefono());
        c.setDireccion(dto.getDireccion());
        return c;
    }
}