package com.accenture.springboot.products.domain;

import com.accenture.springboot.common.domain.Currency;

public record CreditAccount(
        CBU cbu,
        Currency currency,
        Double balance,
        Double limit
) implements Product, Account, CreditProduct {
}