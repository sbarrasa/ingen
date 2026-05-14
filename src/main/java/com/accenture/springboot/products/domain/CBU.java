package com.accenture.springboot.products.domain;

public record CBU(String value) {

    public CBU(String value) {
        this.value = value;
        validateNotNull();
        validateNotEmpty();
        validateDigits();
        validateLength();
    }

    private void validateDigits() {
        if (!value.matches("\\d+")) {
            throw new IllegalArgumentException("CBU must contain only digits");
        }
    }

    private void validateLength() {
        if (value.length() != 22) {
            throw new IllegalArgumentException("CBU must have 22 digits");
        }
    }

    private void validateNotEmpty() {
        if (value.isBlank()) {
            throw new IllegalArgumentException("CBU cannot be empty");
        }
    }

    private void validateNotNull() {
        if (value == null) {
            throw new IllegalArgumentException("CBU cannot be null");
        }
    }
}