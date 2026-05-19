package com.accenture.powerbank.movements.service;

public class MovementNotFoundException extends RuntimeException {

    public MovementNotFoundException(Long id) {
        super("Movement %s not found".formatted(id));
    }
}
