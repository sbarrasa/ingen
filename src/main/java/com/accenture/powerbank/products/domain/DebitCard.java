package com.accenture.powerbank.products.domain;

import java.time.LocalDate;

public record DebitCard(
        CardNumber cardNumber,
        Brand brand,
        Integer cvv,
        LocalDate startDate,
        LocalDate expirationDate
) implements Product, Card {
}