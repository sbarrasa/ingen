package com.accenture.springboot.user.service;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Integer id) {
        super("User not found with id: " + id);
    }
}
