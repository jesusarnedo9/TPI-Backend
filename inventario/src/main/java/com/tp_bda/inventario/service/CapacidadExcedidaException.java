package com.tp_bda.inventario.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Mapea esta excepción a HTTP 409 CONFLICT
@ResponseStatus(HttpStatus.CONFLICT)
public class CapacidadExcedidaException extends RuntimeException {
    public CapacidadExcedidaException(String message) {
        super(message);
    }
}