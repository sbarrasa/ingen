package com.accenture.springboot.exception;

public class UserException extends RuntimeException {

    public UserAlreadyExistsException(Integer id) {
        super("User already exists with id: " + id);
    }
    public UserNotFoundException(Integer id) {
        super("User not found with id: " + id);
    }
}
