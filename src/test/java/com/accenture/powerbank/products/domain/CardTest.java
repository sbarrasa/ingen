package com.accenture.powerbank.products.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    //TODO: @Nestor crear validación de vigencia de tarjeta en Card

    @Test
    void startDateLessThanExpirationDate() {
        assertThrows(RuntimeException.class, () -> new DebitCard(
                new CardNumber("9999000011112222"),
                Brand.MC,
                789,
                LocalDate.now().plusYears(1),
                LocalDate.now()
            )
        );

    }



}