package com.accenture.powerbank.products.domain;

import com.accenture.powerbank.common.domain.Currency;

public record SavingAccount(
        CBU cbu,
        Currency currency,
        Double balance
) implements Account, Product { }
