package com.accenture.springboot.common.domain;

public enum Currency {

    ARS("Peso Argentino"),
    USD("Dolar americano"),
    EUR("Euro");

    private final String description;

    Currency(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}