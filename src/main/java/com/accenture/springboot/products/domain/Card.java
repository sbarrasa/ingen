package com.accenture.springboot.products.domain;

import java.time.LocalDate;

public sealed interface Card extends Product
        permits CreditCard, DebitCard {

    CardNumber cardNumber();

    Brand brand();

    Integer cvv();

    LocalDate startDate();

    LocalDate expirationDate();
}