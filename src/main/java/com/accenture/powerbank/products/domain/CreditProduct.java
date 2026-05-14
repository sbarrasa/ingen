package com.accenture.powerbank.products.domain;

public sealed interface CreditProduct permits CreditAccount, CreditCard {
    Double limit();
}
