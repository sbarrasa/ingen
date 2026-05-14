package com.accenture.springboot.products.domain;

public record CardNumber(String value) {

    public CardNumber(String value) {
        this.value = value;
        validateNotNull();
        validateNotEmpty();
        validateDigits();
    }

    private void validateDigits() {
        if (!value.matches("\\d+")) {
            throw new IllegalArgumentException("Card number must contain only digits");
        }
    }

    private void validateNotEmpty() {
        if (value.isBlank()) {
            throw new IllegalArgumentException("Card number cannot be empty");
        }
    }

    private void validateNotNull() {
        if (value == null) {
            throw new IllegalArgumentException("Card number cannot be null");
        }
    }

    private void validateLength() {
        if (value.length() != 16) {
            throw new IllegalArgumentException("Card number must have 16 digits");
        }
    }
}