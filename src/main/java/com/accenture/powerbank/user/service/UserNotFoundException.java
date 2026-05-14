package com.accenture.powerbank.user.service;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Integer id) {
        super("User %s not found".formatted(id));
    }
}
