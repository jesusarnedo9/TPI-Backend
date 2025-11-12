package com.tp_bda.usuarios.service;

import com.tp_bda.usuarios.dto.TransportistaDTO;
import com.tp_bda.usuarios.model.Transportista;
import com.tp_bda.usuarios.repository.TransportistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransportistaService {

    @Autowired
    private TransportistaRepository transportistaRepository;

    // Guardar un nuevo transportista
    public TransportistaDTO guardar(TransportistaDTO dto) {
        Transportista t = fromDto(dto);
        Transportista guardado = transportistaRepository.save(t);
        return toDto(guardado);
    }

    // Buscar por ID (Usado por el ADMIN)
    public TransportistaDTO buscarPorId(Integer id) {
        Transportista t = transportistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transportista no encontrado"));
        return toDto(t);
    }

    // Buscar por ID de Keycloak (Usado por /transportistas/mi-perfil)
    public TransportistaDTO buscarPorKeycloakId(String keycloakUserId) {
        Transportista t = transportistaRepository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new RuntimeException("Perfil de Transportista no encontrado"));
        return toDto(t);
    }

    // Actualizar transportista
    public TransportistaDTO actualizar(Integer id, TransportistaDTO dtoActualizado) {
        Transportista existente = transportistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transportista no encontrado"));

        if (dtoActualizado.getNombre() != null) existente.setNombre(dtoActualizado.getNombre());
        if (dtoActualizado.getApellido() != null) existente.setApellido(dtoActualizado.getApellido());
        if (dtoActualizado.getDni() != null) existente.setDni(dtoActualizado.getDni());
        if (dtoActualizado.getTelefono() != null) existente.setTelefono(dtoActualizado.getTelefono());

        Transportista actualizado = transportistaRepository.save(existente);
        return toDto(actualizado);
    }

    // Listar todos
    public List<TransportistaDTO> listar() {
        return transportistaRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Conversión Entidad -> DTO
    private TransportistaDTO toDto(Transportista t) {
        TransportistaDTO dto = new TransportistaDTO();
        dto.setId(t.getId());
        dto.setKeycloakUserId(t.getKeycloakUserId());
        dto.setNombre(t.getNombre());
        dto.setApellido(t.getApellido());
        dto.setDni(t.getDni());
        dto.setTelefono(t.getTelefono());
        return dto;
    }

    // Conversión DTO -> Entidad
    private Transportista fromDto(TransportistaDTO dto) {
        Transportista t = new Transportista();
        //t.setId(dto.getId());
        t.setKeycloakUserId(dto.getKeycloakUserId());
        t.setNombre(dto.getNombre());
        t.setApellido(dto.getApellido());
        t.setDni(dto.getDni());
        t.setTelefono(dto.getTelefono());
        return t;
    }
}

