package com.tp_bda.inventario.service;

import com.tp_bda.inventario.dto.DepositoDTO;
import com.tp_bda.inventario.model.Deposito;
import com.tp_bda.inventario.repository.DepositoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepositoService {

    @Autowired
    private DepositoRepository depositoRepository;

    @Transactional
    public DepositoDTO guardar(DepositoDTO dto) {
        Deposito d = fromDto(dto); // DTO no debe tener ID
        Deposito guardado = depositoRepository.save(d);
        return toDto(guardado);
    }

    @Transactional(readOnly = true)
    public DepositoDTO buscarPorId(Integer id) {
        Deposito d = depositoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Depósito no encontrado con id: " + id));
        return toDto(d);
    }

    @Transactional
    public DepositoDTO actualizar(Integer id, DepositoDTO dtoActualizado) {
        Deposito existente = depositoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Depósito no encontrado con id: " + id));

        // Actualización campo por campo
        if (dtoActualizado.getNombre() != null) existente.setNombre(dtoActualizado.getNombre());
        if (dtoActualizado.getDireccion() != null) existente.setDireccion(dtoActualizado.getDireccion());
        if (dtoActualizado.getLatitud() != null) existente.setLatitud(dtoActualizado.getLatitud());
        if (dtoActualizado.getLongitud() != null) existente.setLongitud(dtoActualizado.getLongitud());
        if (dtoActualizado.getCostoEstadiaDiaria() != null) existente.setCostoEstadiaDiaria(dtoActualizado.getCostoEstadiaDiaria());

        Deposito actualizado = depositoRepository.save(existente);
        return toDto(actualizado);
    }

    @Transactional(readOnly = true)
    public List<DepositoDTO> listar() {
        return depositoRepository.findAll()
                .stream()
                .map(DepositoService::toDto) // Usando referencia de método estático
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminar(Integer id) {
        if (!depositoRepository.existsById(id)) {
            throw new EntityNotFoundException("Depósito no encontrado con id: " + id);
        }
        depositoRepository.deleteById(id);
    }

    // --- MAPPERS ESTÁTICOS ---

    // Entidad -> DTO
    private static DepositoDTO toDto(Deposito d) {
        DepositoDTO dto = new DepositoDTO();
        dto.setId(d.getId());
        dto.setNombre(d.getNombre());
        dto.setDireccion(d.getDireccion());
        dto.setLatitud(d.getLatitud());
        dto.setLongitud(d.getLongitud());
        dto.setCostoEstadiaDiaria(d.getCostoEstadiaDiaria());
        return dto;
    }

    // DTO -> Entidad
    private static Deposito fromDto(DepositoDTO dto) {
        Deposito d = new Deposito();
        // Omitimos el ID, ya que es para una entidad NUEVA
        d.setNombre(dto.getNombre());
        d.setDireccion(dto.getDireccion());
        d.setLatitud(dto.getLatitud());
        d.setLongitud(dto.getLongitud());
        d.setCostoEstadiaDiaria(dto.getCostoEstadiaDiaria());
        return d;
    }
}