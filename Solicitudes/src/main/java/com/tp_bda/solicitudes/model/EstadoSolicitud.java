package com.tp_bda.solicitudes.model;

public enum EstadoSolicitud {
    BORRADOR,
    PENDIENTE,
    PROGRAMADA, // Ruta y recursos asignados
    EN_PROGRESO, // Primer tramo iniciado
    FINALIZADA, // Último tramo completado y registro final
    CANCELADA
}
