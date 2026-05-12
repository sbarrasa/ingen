package com.accenture.springboot.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(Integer id) {
        super("User already exists with id: " + id);
    }
}
