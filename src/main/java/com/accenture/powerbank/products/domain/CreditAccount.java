package com.accenture.powerbank.products.domain;

import com.accenture.powerbank.common.domain.Currency;

public record CreditAccount(
        CBU cbu,
        Currency currency,
        Double balance,
        Double limit
) implements Product, Account, CreditProduct {
}