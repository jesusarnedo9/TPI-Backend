package com.tp_bda.inventario.service;

import com.tp_bda.inventario.dto.ContenedorDTO;
import com.tp_bda.inventario.model.Contenedor;
import com.tp_bda.inventario.model.Deposito;
import com.tp_bda.inventario.repository.ContenedorRepository;
import com.tp_bda.inventario.repository.DepositoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContenedorService {

    @Autowired
    private ContenedorRepository contenedorRepository;

    @Autowired
    private DepositoRepository depositoRepository; // Necesario para asignar el depósito

    @Transactional
    public ContenedorDTO guardar(ContenedorDTO dto) {
        Contenedor c = fromDto(dto); // Mapea campos simples

        // Lógica de negocio: Asigna el depósito si se provee un ID
        if (dto.getDepositoId() != null) {
            Deposito d = depositoRepository.findById(dto.getDepositoId())
                    .orElseThrow(() -> new EntityNotFoundException("Depósito no encontrado con id: " + dto.getDepositoId()));
            c.setDeposito(d);
        } else {
            // Si no hay ID de depósito, nos aseguramos que esté en null
            c.setDeposito(null);
        }

        Contenedor guardado = contenedorRepository.save(c);
        return toDto(guardado);
    }

    @Transactional(readOnly = true)
    public ContenedorDTO buscarPorId(Integer id) {
        Contenedor c = contenedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contenedor no encontrado con id: " + id));
        return toDto(c);
    }

    @Transactional
    public ContenedorDTO actualizar(Integer id, ContenedorDTO dtoActualizado) {
        Contenedor existente = contenedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contenedor no encontrado con id: " + id));

        // Actualización de campos
        if (dtoActualizado.getPeso() != null) existente.setPeso(dtoActualizado.getPeso());
        if (dtoActualizado.getVolumen() != null) existente.setVolumen(dtoActualizado.getVolumen());
        if (dtoActualizado.getEstado() != null) existente.setEstado(dtoActualizado.getEstado());
        if (dtoActualizado.getClienteId() != null) existente.setClienteId(dtoActualizado.getClienteId());

        // Lógica de negocio: Actualizar el depósito
        if (dtoActualizado.getDepositoId() != null) {
            Deposito d = depositoRepository.findById(dtoActualizado.getDepositoId())
                    .orElseThrow(() -> new EntityNotFoundException("Depósito no encontrado con id: " + dtoActualizado.getDepositoId()));
            existente.setDeposito(d);
        } else {
            existente.setDeposito(null); // Permite desasociar de un depósito
        }

        Contenedor actualizado = contenedorRepository.save(existente);
        return toDto(actualizado);
    }

    @Transactional(readOnly = true)
    public List<ContenedorDTO> listar() {
        return contenedorRepository.findAll()
                .stream()
                .map(ContenedorService::toDto) // Usamos el mapper estático
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!contenedorRepository.existsById(id)) {
            throw new EntityNotFoundException("Contenedor no encontrado con id: " + id);
        }
        contenedorRepository.deleteById(id);
    }

    // --- MAPPERS ESTÁTICOS (Movidos aquí desde el DTO) ---

    // Entidad -> DTO
    private static ContenedorDTO toDto(Contenedor c) {
        ContenedorDTO dto = new ContenedorDTO();
        dto.setId(c.getId());
        dto.setPeso(c.getPeso());
        dto.setVolumen(c.getVolumen());
        dto.setEstado(c.getEstado());
        dto.setClienteId(c.getClienteId());

        // Importante: Manejar el depósito nulo
        if (c.getDeposito() != null) {
            dto.setDepositoId(c.getDeposito().getId());
        } else {
            dto.setDepositoId(null);
        }
        return dto;
    }

    // DTO -> Entidad (Mapeo simple, la relación se maneja en el servicio)
    private static Contenedor fromDto(ContenedorDTO dto) {
        Contenedor c = new Contenedor();
        // Omitimos el ID para la creación
        c.setPeso(dto.getPeso());
        c.setVolumen(dto.getVolumen());
        c.setEstado(dto.getEstado());
        c.setClienteId(dto.getClienteId());
        // El 'deposito' se asigna en el método 'guardar' o 'actualizar'
        return c;
    }
}