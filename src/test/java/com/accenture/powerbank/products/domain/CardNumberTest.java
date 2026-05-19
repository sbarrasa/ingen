package com.accenture.powerbank.products.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CardNumberTest {

    @Test
    void createsCardNumberWhenValueIsValid() {
        var cardNumber = new CardNumber("1111222233334444");

        assertEquals("1111222233334444", cardNumber.value());
    }

    @Test
    void throwsWhenValueIsNull() {
        var exception = assertThrows(IllegalArgumentException.class, () -> new CardNumber(null));

        assertEquals("Card number cannot be null", exception.getMessage());
    }

    @Test
    void throwsWhenValueIsBlank() {
        var exception = assertThrows(IllegalArgumentException.class, () -> new CardNumber("   "));

        assertEquals("Card number cannot be empty", exception.getMessage());
    }

    @Test
    void throwsWhenValueContainsNonDigits() {
        var exception = assertThrows(IllegalArgumentException.class, () -> new CardNumber("111122223333444a"));

        assertEquals("Card number must contain only digits", exception.getMessage());
    }

    @Test
    void throwsWhenValueDoesNotHaveSixteenDigits() {
        var exception = assertThrows(IllegalArgumentException.class, () -> new CardNumber("111122223333444"));

        assertEquals("Card number must have 16 digits", exception.getMessage());
    }
}
