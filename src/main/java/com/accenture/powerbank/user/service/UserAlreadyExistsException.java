package com.accenture.powerbank.user.service;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(Integer id) {
        super("User %s already exists".formatted(id));
    }
}
