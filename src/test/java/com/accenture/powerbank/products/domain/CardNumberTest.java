package com.accenture.powerbank.products.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardNumberTest {
    @Test
    void cardNumberLength(){
        var cardNumber = new CardNumber("1111222233334444");
        assertEquals(16, cardNumber.value().length());
    }

    @Test
    void createFailByNull(){
        assertThrows(IllegalArgumentException.class,
                () -> new CardNumber(null));

    }


    @Test
    void createFailByLength(){
        assertThrows(IllegalArgumentException.class,
                () -> new CardNumber("1234"));

    }
}