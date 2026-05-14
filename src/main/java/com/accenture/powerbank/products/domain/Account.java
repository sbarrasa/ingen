package com.accenture.powerbank.products.domain;

import com.accenture.powerbank.common.domain.Currency;

public sealed interface Account extends Product
        permits SavingAccount, CreditAccount {

    CBU cbu();

    Currency currency();

    Double balance();
}
