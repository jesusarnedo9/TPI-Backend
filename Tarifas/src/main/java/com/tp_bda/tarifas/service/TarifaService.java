package com.tp_bda.tarifas.service;

import com.tp_bda.tarifas.model.Tarifa;
import com.tp_bda.tarifas.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TarifaService {

    @Autowired
    private TarifaRepository tarifaRepository;

    private static final double PRECIO_POR_KM = 1.5;
    private static final double PRECIO_POR_KG = 0.005;
    private static final double VELOCIDAD_PROMEDIO_KMH = 80.0;


    // ----------------------------------------------------------------------
    // FUNCIONALIDAD 1: CÁLCULO DE ESTIMACIÓN (Usado por MS Solicitudes)
    // ----------------------------------------------------------------------

    /**
     * Calcula el costo estimado de un envío basándose en distancia, peso y volumen.
     */
    public Double estimarCosto(Double distanciaKm, Double peso, Double volumen) {

        // Consultamos tarifas base de la BDD o usamos valores por defecto si no existen
        double costoPorKm = tarifaRepository.findByTipo("COSTO_KM")
                .map(tarifa -> tarifa.getValor().doubleValue())
                .orElse(1.5);

        double costoPorKg = tarifaRepository.findByTipo("COSTO_KG")
                .map(tarifa -> tarifa.getValor().doubleValue())
                .orElse(0.1);

        double costoPorVolumen = tarifaRepository.findByTipo("COSTO_VOLUMEN")
                .map(tarifa -> tarifa.getValor().doubleValue())
                .orElse(0.5);

        double tarifaBase = tarifaRepository.findByTipo("TARIFA_BASE")
                .map(tarifa -> tarifa.getValor().doubleValue())
                .orElse(50.0);

        // Fórmula de cálculo
        double costoTotal = tarifaBase +
                (distanciaKm * costoPorKm) +
                (peso * costoPorKg) +
                (volumen * costoPorVolumen);

        return costoTotal;
    }

    /**
     * Calcula el costo Y TIEMPO estimados para el MS Viajes.
     * Devuelve un Map con "costoEstimado" y "tiempoEstimadoHoras".
     */
    public Map<String, Double> calcularTarifaAproximada(Double distanciaKm, Double pesoContenedor) {

        if (distanciaKm == null || pesoContenedor == null) {
            throw new IllegalArgumentException("Distancia y peso son requeridos.");
        }

        // Esta es la lógica que tu TarifasClient estaba "simulando" en el catch
        double costoEstimado = (distanciaKm * PRECIO_POR_KM) + (pesoContenedor * PRECIO_POR_KG);
        double tiempoEstimadoHoras = distanciaKm / VELOCIDAD_PROMEDIO_KMH;

        // Devuelve el Map exacto que el TarifasClient espera
        return Map.of(
                "costoEstimado", Math.round(costoEstimado * 100.0) / 100.0,
                "tiempoEstimadoHoras", Math.round(tiempoEstimadoHoras * 100.0) / 100.0
        );
    }

    // ----------------------------------------------------------------------
    // FUNCIONALIDAD 2: GESTIÓN DE PARÁMETROS BASE (Usado por MS Operador/Admin)
    // ----------------------------------------------------------------------

    /**
     * Guarda o actualiza un parámetro de tarifa base.
     */
    public Tarifa guardarTarifa(Tarifa tarifa) {
        return tarifaRepository.save(tarifa);
    }

    /**
     * Obtiene todas las tarifas base existentes.
     */
    public List<Tarifa> obtenerTodasLasTarifas() {
        return tarifaRepository.findAll();
    }

    /**
     * Obtiene una tarifa específica por ID.
     */
    public Optional<Tarifa> obtenerTarifaPorId(Long id) {
        return tarifaRepository.findById(id);
    }
}