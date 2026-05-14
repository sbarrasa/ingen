package com.accenture.powerbank.products.domain;

import java.time.LocalDate;

public record CreditCard(
        CardNumber cardNumber,
        Brand brand,
        String tierLevel,
        Integer cvv,
        LocalDate startDate,
        LocalDate expirationDate,
        Double limit
) implements Product, Card, CreditProduct {
}