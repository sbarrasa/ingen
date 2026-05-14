package com.accenture.powerbank.products.domain;

public enum Brand {

    VISA("VISA"),
    MC("MasterCard"),
    AMEX("American Express"),
    CABAL("CABAL");

    private final String description;

    Brand(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}