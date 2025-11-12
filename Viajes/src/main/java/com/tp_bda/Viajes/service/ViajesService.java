package com.tp_bda.Viajes.service;

import com.tp_bda.Viajes.client.InventarioClient;
import com.tp_bda.Viajes.client.MapClient;
import com.tp_bda.Viajes.client.TarifasClient;
import com.tp_bda.Viajes.repository.RutasRepository;
import com.tp_bda.Viajes.repository.TramosRepository;
import com.tp_bda.Viajes.models.Tramos;
import com.tp_bda.Viajes.models.EstadoTramo;
import com.tp_bda.Viajes.service.mapper.ViajesMapper;
import com.tp_bda.Viajes.web.api.dto.RutaTentativaDto;
import com.tp_bda.Viajes.web.api.dto.TramoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ViajesService {

    @Autowired
    private MapClient mapsClient; // Nuevo cliente de Mapas
    @Autowired
    private TarifasClient tarifasClient; // Nuevo cliente de Tarifas

    @Autowired
    private RutasRepository rutasRepository;

    @Autowired
    private TramosRepository tramosRepository;

    @Autowired
    private InventarioClient inventarioClient;

    @Autowired
    private ViajesMapper mapper; // INYECCIÓN DEL MAPPER

    // NOTA: Aquí faltaría inyectar MapsClient y TarifasClient para completar los requisitos.


    /**
     * Requerimiento: PUT /tramos/{tramoid}/inicio
     * Determina el inicio de un tramo.
     * @param tramoId ID del tramo.
     * @return TramoDTO actualizado.
     */
    public TramoDto iniciarTramo(Integer tramoId) {
        Tramos tramo = tramosRepository.findById(tramoId)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado"));

        if (tramo.getEstado() != EstadoTramo.ASIGNADO) {
            throw new IllegalStateException("El tramo no puede ser iniciado, su estado actual es: " + tramo.getEstado());
        }

        tramo.setEstado(EstadoTramo.INICIADO);
        tramo.setFechaHoraInicio(LocalDateTime.now());
        // Lógica de logs: Se debe registrar el inicio real del tramo.
        System.out.println("LOG: Tramo " + tramoId + " iniciado por el camión " + tramo.getCamionId() + " a las " + tramo.getFechaHoraInicio());

        Tramos savedTramo = tramosRepository.save(tramo);
        return mapper.toDTO(savedTramo); // Convierte a DTO
    }

    /**
     * Requerimiento: PUT /tramos/{tramoid}/fin
     * Determina el fin de un tramo.
     * @param tramoId ID del tramo.
     * @return TramoDTO actualizado.
     */
    public TramoDto finalizarTramo(Integer tramoId) {
        Tramos tramo = tramosRepository.findById(tramoId)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado"));

        if (tramo.getEstado() != EstadoTramo.INICIADO) {
            throw new IllegalStateException("El tramo no puede ser finalizado, su estado actual es: " + tramo.getEstado());
        }

        tramo.setEstado(EstadoTramo.FINALIZADO);
        tramo.setFechaHoraFin(LocalDateTime.now());
        // Lógica de logs: Se debe registrar el fin real del tramo.
        System.out.println("LOG: Tramo " + tramoId + " finalizado a las " + tramo.getFechaHoraFin());

        // NOTA: Aquí iría la llamada al Microservicio Tarifas para cálculo de costo real.

        Tramos savedTramo = tramosRepository.save(tramo);
        return mapper.toDTO(savedTramo); // Convierte a DTO
    }

    /**
     * Requerimiento: POST /rutas/{tramoId} (Asignar Camión a Tramo)
     * Asigna un camión a un tramo de traslado y valida su capacidad.
     */
    public TramoDto asignarCamionATramo(Integer tramoId, Integer camionId, Double pesoContenedor, Double volumenContenedor) {
        // 1. Validar Capacidad del Camión (Llamada a Inventario MS)
        boolean esApto = inventarioClient.validarCapacidadCamion(camionId, pesoContenedor, volumenContenedor);

        if (!esApto) {
            throw new IllegalArgumentException("El camión ID " + camionId + " no es apto por capacidad (peso/volumen) o la validación falló.");
        }

        // 2. Asignar camión y cambiar estado
        Tramos tramo = tramosRepository.findById(tramoId)
                .orElseThrow(() -> new RuntimeException("Tramo no encontrado"));

        if (tramo.getCamionId() != null) {
            throw new IllegalStateException("El tramo ya tiene un camión asignado.");
        }

        tramo.setCamionId(camionId);
        tramo.setEstado(EstadoTramo.ASIGNADO);

        // 3. Persistir y devolver
        Tramos savedTramo = tramosRepository.save(tramo);
        return mapper.toDTO(savedTramo); // Convierte a DTO
    }
    /**
     * Requerimiento: GET /rutas/tentativas
     * Consulta rutas tentativas (tramos sugeridos, tiempo y costo estimados).
     */
    public RutaTentativaDto getRutaTentativa(
            String origen, double latOrigen, double lonOrigen,
            String destino, double latDestino, double lonDestino,
            Double pesoContenedor)
    {
        // 1. Calcular distancia (Llamada a API Externa)
        Double distanciaKm = mapsClient.getDistancia(latOrigen, lonOrigen, latDestino, lonDestino);

        if (distanciaKm == null || distanciaKm <= 0) {
            throw new RuntimeException("No se pudo calcular la distancia o la ruta es inválida.");
        }

        // 2. Calcular costos y tiempo estimados (Llamada al MS Tarifas)
        Map<String, Double> estimaciones = tarifasClient.calcularTarifaEstimada(distanciaKm, pesoContenedor);

        // 3. Devolver DTO
        return new RutaTentativaDto(
                origen,
                destino,
                distanciaKm,
                estimaciones.getOrDefault("tiempoEstimadoHoras", 0.0),
                estimaciones.getOrDefault("costoEstimado", 0.0)
        );
    }

    /**
     * Requerimiento: GET /rutas/asignadas/transportista/{id}
     * Consulta los tramos asignados a un transportista.
     * @param transportistaId ID del transportista (FK al MS Usuarios)
     * @return Lista de TramosDTO.
     */
    public List<TramoDto> getTramosAsignadosPorTransportista(Integer transportistaId) {
        // NOTA: Asumimos que Inventario tiene un endpoint para obtener el ID del camión
        // asociado a un transportista, o que el CamionEntity tiene el campo transportistaId.
        // Aquí simplemente usamos el ID como CamionId para demostrar la consulta.

        // 1. Buscar tramos en estado ASIGNADO/INICIADO
        List<Tramos> tramos = tramosRepository.findByCamionId(transportistaId); // Usando findByCamionId

        // 2. Filtrar solo los que están activos para el transportista (ASIGNADO o INICIADO)
        List<Tramos> tramosActivos = tramos.stream()
                .filter(t -> t.getEstado() == EstadoTramo.ASIGNADO || t.getEstado() == EstadoTramo.INICIADO)
                .collect(Collectors.toList());

        // 3. Convertir a DTO y devolver
        return tramosActivos.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}