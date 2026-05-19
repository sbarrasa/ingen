package com.accenture.powerbank.common.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Set;
import java.util.regex.Pattern;

public record Cuit(String value) {

    private static final Pattern CUIT_PATTERN = Pattern.compile("\\d{11}");
    private static final Set<String> VALID_PREFIXES = Set.of(
            "20", "23", "24", "27", "30", "33", "34"
    );

    private static final int[] WEIGHTS = {
            5, 4, 3, 2, 7, 6, 5, 4, 3, 2
    };

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public Cuit {
        value = normalize(value);
        validate(value);
    }

    public static Cuit of(String value) {
        return new Cuit(value);
    }

    private static String normalize(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new IllegalArgumentException("CUIT is required");
        }

        return rawValue
                .trim()
                .replace("-", "")
                .replace(".", "")
                .replace(" ", "");
    }

    private static void validate(String normalizedValue) {
        if (!CUIT_PATTERN.matcher(normalizedValue).matches()) {
            throw new IllegalArgumentException(
                    "Invalid CUIT format. Expected 11 digits or format XX-XXXXXXXX-X"
            );
        }

        String prefix = normalizedValue.substring(0, 2);

        if (!VALID_PREFIXES.contains(prefix)) {
            throw new IllegalArgumentException(
                    "Invalid CUIT prefix. Allowed prefixes are: 20, 23, 24, 27, 30, 33, 34"
            );
        }

        if (!hasValidCheckDigit(normalizedValue)) {
            throw new IllegalArgumentException("Invalid CUIT check digit");
        }
    }

    private static boolean hasValidCheckDigit(String normalizedValue) {
        int sum = 0;

        for (int i = 0; i < WEIGHTS.length; i++) {
            int digit = Character.digit(normalizedValue.charAt(i), 10);
            sum += digit * WEIGHTS[i];
        }

        int expectedDigit = 11 - (sum % 11);

        if (expectedDigit == 11) {
            expectedDigit = 0;
        }

        if (expectedDigit == 10) {
            expectedDigit = 9;
        }

        int actualDigit = Character.digit(normalizedValue.charAt(10), 10);

        return expectedDigit == actualDigit;
    }

    public String formatted() {
        return value.substring(0, 2)
                + "-"
                + value.substring(2, 10)
                + "-"
                + value.substring(10);
    }

    @JsonValue
    public String jsonValue() {
        return value;
    }
}