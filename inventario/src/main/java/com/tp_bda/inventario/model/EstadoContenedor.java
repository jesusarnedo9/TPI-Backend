package com.tp_bda.inventario.model;

/**
 * Define los posibles estados de un contenedor.
 */
public enum EstadoContenedor {
    EN_DEPOSITO, // Recién ingresado o almacenado
    RESERVADO,   // Asignado a una solicitud de transporte, esperando ser recogido
    EN_TRANSITO, // En viaje (sobre un camión)
    ENTREGADO    // Llegó a su destino final
}