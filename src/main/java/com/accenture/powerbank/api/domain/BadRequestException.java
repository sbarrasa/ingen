package com.accenture.powerbank.api.domain;

public class BadRequestException extends BusinessException {

    public BadRequestException(String message) {
        super(message);
    }
}
