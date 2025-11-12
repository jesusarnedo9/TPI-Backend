package com.tp_bda.solicitudes.service; // Asegúrate de que el package sea el correcto

import com.tp_bda.solicitudes.client.InventarioClient; // Importa tus nuevos clientes
import com.tp_bda.solicitudes.client.TarifasClient;
import com.tp_bda.solicitudes.client.ViajesClient;
import com.tp_bda.solicitudes.dto.SolicitudRequestDTO; // Importa tus DTOs
import com.tp_bda.solicitudes.dto.SolicitudResponseDTO;
import com.tp_bda.solicitudes.model.EstadoSolicitud;
import com.tp_bda.solicitudes.model.Solicitudes;
import com.tp_bda.solicitudes.repository.SolicitudesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt; // Import para JWT
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SolicitudesService {

    @Autowired
    private SolicitudesRepository solicitudesRepository;

    // --- REFACTORIZADO ---
    // Inyectamos los Clientes específicos en lugar de RestTemplate
    @Autowired
    private InventarioClient inventarioClient;

    @Autowired
    private ViajesClient viajesClient;

    @Autowired
    private TarifasClient tarifasClient;

    /**
     * Orquesta la creación de una solicitud (POST)
     */
    public SolicitudResponseDTO crearSolicitud(SolicitudRequestDTO request, Jwt jwt) {

        // 1. Validar Cliente
        Integer clienteId = Integer.parseInt(jwt.getClaimAsString("clienteId"));

        // 2. Llamar a MS Inventario (usando el cliente)
        Integer contenedorId = inventarioClient.crearContenedor(
                request.getPeso(),
                request.getVolumen(),
                request.getDescripcionCarga()
        );
        if (contenedorId == null) {
            throw new RuntimeException("No se pudo crear el contenedor en Inventario.");
        }

        // 3. Llamar a MS Viajes (usando el cliente)
        Map<String, Object> rutaResponse = viajesClient.crearRutaTentativa(
                request.getDireccionOrigen(),
                request.getDireccionDestino()
        );
        if (rutaResponse == null || !rutaResponse.containsKey("id") || !rutaResponse.containsKey("distanciaKm")) {
            throw new RuntimeException("No se pudo crear la ruta tentativa en Viajes.");
        }
        Integer rutaId = (Integer) rutaResponse.get("id");
        Double distanciaKm = (Double) rutaResponse.get("distanciaKm");


        // 4. Llamar a MS Tarifas (usando el cliente)
        Double costoAproximado = tarifasClient.estimarTarifa(
                distanciaKm,
                request.getPeso(),
                request.getVolumen()
        );
        if (costoAproximado == null) {
            throw new RuntimeException("No se pudo calcular la tarifa.");
        }

        // 5. Guardar la Solicitud en la DB local (Lógica de negocio)
        Solicitudes nuevaSolicitud = new Solicitudes();
        nuevaSolicitud.setClienteId(clienteId);
        nuevaSolicitud.setContenedorId(contenedorId);
        nuevaSolicitud.setRutaId(rutaId);
        nuevaSolicitud.setCostoEstimado(BigDecimal.valueOf(costoAproximado));
        nuevaSolicitud.setEstado(EstadoSolicitud.PENDIENTE);
        nuevaSolicitud.setFechaHoraSalida(request.getFechaHoraSalida());

        Solicitudes solicitudGuardada = solicitudesRepository.save(nuevaSolicitud);

        // 6. Mapear y devolver la respuesta
        return mapToResponseDTO(solicitudGuardada);
    }

    // --- MÉTODO PARA CONSULTAR (GET) ---

    /**
     * Consulta todas las solicitudes de un cliente específico (GET)
     */
    public List<SolicitudResponseDTO> consultarPorClienteId(Integer clienteId) {
        // Llama al nuevo método del repositorio
        List<Solicitudes> solicitudes = solicitudesRepository.findByClienteId(clienteId);

        // Convierte la lista de Entidades a una lista de DTOs
        return solicitudes.stream()
                .map(this::mapToResponseDTO) // Re-utiliza el mapeador
                .collect(Collectors.toList());
    }


    /**
     * Mapeador privado para convertir Entidad a DTO
     */
    private SolicitudResponseDTO mapToResponseDTO(Solicitudes entity) {
        SolicitudResponseDTO dto = new SolicitudResponseDTO();
        dto.setSolicitudId(entity.getId());
        dto.setEstado(entity.getEstado().name()); // Convierte Enum a String
        dto.setFechaHoraSalida(entity.getFechaHoraSalida());
        dto.setClienteId(entity.getClienteId());
        dto.setContenedorId(entity.getContenedorId());
        dto.setRutaId(entity.getRutaId());

        if (entity.getCostoEstimado() != null) {
            dto.setCostoAproximado(entity.getCostoEstimado().doubleValue());
        }

        return dto;
    }
}