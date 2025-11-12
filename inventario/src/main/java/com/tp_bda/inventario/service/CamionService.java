package com.tp_bda.inventario.service;

import com.tp_bda.inventario.dto.CamionDTO;
import com.tp_bda.inventario.model.Camion;
import com.tp_bda.inventario.repository.CamionRepository;
import jakarta.persistence.EntityNotFoundException; // Importante
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
public class CamionService {

    @Autowired
    private CamionRepository camionRepository;

    // Crear nuevo camión
    @Transactional
    public CamionDTO guardar(CamionDTO dto) {
        Camion c = fromDto(dto); // DTO no debe tener ID
        Camion guardado = camionRepository.save(c);
        return toDto(guardado);
    }

    // Buscar camión por ID
    @Transactional(readOnly = true)
    public CamionDTO buscarPorId(Integer id) {
        Camion c = camionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Camión no encontrado con id: " + id));
        return toDto(c);
    }

    // Actualizar camión
    @Transactional
    public CamionDTO actualizar(Integer id, CamionDTO dtoActualizado) {
        Camion existente = camionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Camión no encontrado con id: " + id));

        // Actualización campo por campo
        if (dtoActualizado.getDominio() != null) existente.setDominio(dtoActualizado.getDominio());
        if (dtoActualizado.getCapacidadPeso() != null) existente.setCapacidadPeso(dtoActualizado.getCapacidadPeso());
        if (dtoActualizado.getCapacidadVolumen() != null) existente.setCapacidadVolumen(dtoActualizado.getCapacidadVolumen());
        if (dtoActualizado.getCostos() != null) existente.setCostos(dtoActualizado.getCostos());
        if (dtoActualizado.getDisponible() != null) existente.setDisponible(dtoActualizado.getDisponible());
        if (dtoActualizado.getTransportistaId() != null) existente.setTransportistaId(dtoActualizado.getTransportistaId());

        Camion actualizado = camionRepository.save(existente);
        return toDto(actualizado);
    }

    // Listar todos los camiones
    @Transactional(readOnly = true)
    public List<CamionDTO> listar() {
        return camionRepository.findAll()
                .stream()
                .map(CamionService::toDto) // Usando referencia de método estático
                .collect(Collectors.toList());
    }

    // Eliminar camión
    @Transactional
    public void eliminar(Integer id) {
        if (!camionRepository.existsById(id)) {
            throw new EntityNotFoundException("Camión no encontrado con id: " + id);
        }
        camionRepository.deleteById(id);
    }

    // Sobrecarga para compatibilidad: acepta Double y delega a la versión BigDecimal
    @Transactional(readOnly = true)
    public void validarCapacidad(Integer id, Double pesoCarga, Double volumenCarga) {
        if (pesoCarga == null || volumenCarga == null) {
            throw new IllegalArgumentException("Los parámetros 'peso' y 'volumen' son requeridos");
        }
        validarCapacidad(id, BigDecimal.valueOf(pesoCarga), BigDecimal.valueOf(volumenCarga));
    }

    @Transactional(readOnly = true) // Es una operación de solo lectura
    public void validarCapacidad(Integer id, BigDecimal pesoCarga, BigDecimal volumenCarga) {

        // 1. Buscar el camión (reutiliza la lógica de búsqueda)
        Camion camion = camionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Camión no encontrado con ID: " + id));
        // Esto será capturado como 404 NOT FOUND

        // 2. Validar Peso (usando los nombres de tu entidad)
        if (camion.getCapacidadPeso().compareTo(pesoCarga) < 0) {
            throw new CapacidadExcedidaException(
                    String.format("La carga (%s kg) excede el peso máximo (%s kg) del camión.",
                            pesoCarga.toPlainString(), camion.getCapacidadPeso().toPlainString())
            );
            // Esto será capturado como 409 CONFLICT
        }

        // 3. Validar Volumen (usando los nombres de tu entidad)
        if (camion.getCapacidadVolumen().compareTo(volumenCarga) < 0) {
            throw new CapacidadExcedidaException(
                    String.format("La carga (%s m³) excede el volumen máximo (%s m³) del camión.",
                            volumenCarga.toPlainString(), camion.getCapacidadVolumen().toPlainString())
            );
            // Esto será capturado como 409 CONFLICT
        }

        // 4. Si pasa ambas validaciones, el método termina exitosamente.
        // El controller devolverá 'true'.
    }

    // --- MAPPERS ESTÁTICOS ---

    // Entidad -> DTO
    private static CamionDTO toDto(Camion c) {
        CamionDTO dto = new CamionDTO();
        dto.setId(c.getId());
        dto.setDominio(c.getDominio());
        dto.setCapacidadPeso(c.getCapacidadPeso());
        dto.setCapacidadVolumen(c.getCapacidadVolumen());
        dto.setCostos(c.getCostos());
        dto.setDisponible(c.getDisponible());
        dto.setTransportistaId(c.getTransportistaId());
        return dto;
    }

    // DTO -> Entidad
    private static Camion fromDto(CamionDTO dto) {
        Camion c = new Camion();
        // Omitimos el ID, ya que es para una entidad NUEVA
        c.setDominio(dto.getDominio());
        c.setCapacidadPeso(dto.getCapacidadPeso());
        c.setCapacidadVolumen(dto.getCapacidadVolumen());
        c.setCostos(dto.getCostos());
        c.setDisponible(dto.getDisponible());
        c.setTransportistaId(dto.getTransportistaId());
        return c;
    }
}