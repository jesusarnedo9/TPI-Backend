package com.tp_bda.Viajes.service.mapper;

import com.tp_bda.Viajes.models.Rutas;
import com.tp_bda.Viajes.models.Tramos;
import com.tp_bda.Viajes.models.EstadoTramo;
import com.tp_bda.Viajes.web.api.dto.RutaDto;
import com.tp_bda.Viajes.web.api.dto.TramoDto;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ViajesMapper {

    // --- CONVERSIONES DE ENTIDAD A DTO ---

    /**
     * Convierte una entidad Tramos a un TramoDTO.
     */
    public TramoDto toDTO(Tramos tramo) {
        if (tramo == null) {
            return null;
        }

        TramoDto dto = new TramoDto();
        dto.setId(tramo.getId());
        dto.setOrigen(tramo.getOrigen());
        dto.setDestino(tramo.getDestino());
        dto.setTipo(tramo.getTipo());

        // Convertimos el Enum a String para el DTO
        if (tramo.getEstado() != null) {
            dto.setEstado(tramo.getEstado().name());
        }

        dto.setCostoAproximado(tramo.getCostoAproximado());
        dto.setCostoReal(tramo.getCostoReal());
        dto.setFechaHoraInicio(tramo.getFechaHoraInicio());
        dto.setFechaHoraFin(tramo.getFechaHoraFin());
        dto.setCamionId(tramo.getCamionId());

        if (tramo.getRuta() != null) {
            dto.setRutaId(tramo.getRuta().getId());
        }

        return dto;
    }

    /**
     * Convierte una entidad Rutas a una RutaDTO.
     */
    public RutaDto toDTO(Rutas ruta) {
        if (ruta == null) {
            return null;
        }

        RutaDto dto = new RutaDto();
        dto.setId(ruta.getId());
        dto.setSolicitudId(ruta.getSolicitudId());
        dto.setCantidadTramos(ruta.getCantidadTramos());
        dto.setCantidadDepositos(ruta.getCantidadDepositos());

        // Mapeamos recursivamente la lista de Tramos a TramoDTOs
        if (ruta.getTramos() != null) {
            dto.setTramos(ruta.getTramos().stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }


    // --- CONVERSIONES DE DTO A ENTIDAD ---

    /**
     * Convierte un TramoDTO a una entidad Tramos (útil para crear o actualizar).
     * Nota: Esta conversión no establece la relación bidireccional "ruta" si la necesitas.
     */
    public Tramos toEntity(TramoDto dto) {
        if (dto == null) {
            return null;
        }

        Tramos entity = new Tramos();
        entity.setId(dto.getId());
        entity.setOrigen(dto.getOrigen());
        entity.setDestino(dto.getDestino());
        entity.setTipo(dto.getTipo());

        // Convertimos el String del DTO a Enum para la Entidad
        if (dto.getEstado() != null) {
            entity.setEstado(EstadoTramo.valueOf(dto.getEstado()));
        }

        entity.setCostoAproximado(dto.getCostoAproximado());
        entity.setCostoReal(dto.getCostoReal());
        entity.setFechaHoraInicio(dto.getFechaHoraInicio());
        entity.setFechaHoraFin(dto.getFechaHoraFin());
        entity.setCamionId(dto.getCamionId());

        // NOTA: La relación "ruta" (ManyToOne) no se puede establecer aquí
        // solo con el rutaId. Tendrías que buscar la entidad Rutas con el ID
        // en tu servicio principal si estás creando un nuevo tramo.

        return entity;
    }
}