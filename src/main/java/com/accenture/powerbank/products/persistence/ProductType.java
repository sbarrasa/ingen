package com.accenture.powerbank.products.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum ProductType {

    DEBIT_CARD,
    CREDIT_CARD,
    SAVING_ACCOUNT,
    CREDIT_ACCOUNT;

    @JsonCreator
    public static ProductType fromJson(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Product type is required. Use field 'type' with DEBIT_CARD, CREDIT_CARD, SAVING_ACCOUNT or CREDIT_ACCOUNT");
        }

        String normalizedValue = value.trim().toUpperCase();
        try {
            return ProductType.valueOf(normalizedValue);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid product type: " + value + ". Allowed values: " + allowedValues());
        }
    }

    @JsonValue
    public String toJson() {
        return name();
    }

    public static String allowedValues() {
        return Arrays.stream(values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
