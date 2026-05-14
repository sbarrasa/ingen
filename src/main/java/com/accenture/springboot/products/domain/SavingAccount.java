package com.accenture.springboot.products.domain;

import com.accenture.springboot.common.domain.Currency;

public record SavingAccount(
        CBU cbu,
        Currency currency,
        Double balance
) implements Account, Product { }
