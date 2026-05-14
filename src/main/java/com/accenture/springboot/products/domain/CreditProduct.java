package com.accenture.springboot.products.domain;

public sealed interface CreditProduct permits CreditAccount, CreditCard {
    Double limit();
}
