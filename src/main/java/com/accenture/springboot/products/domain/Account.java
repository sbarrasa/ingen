package com.accenture.springboot.products.domain;

import com.accenture.springboot.common.domain.Currency;

public sealed interface Account extends Product
        permits SavingAccount, CreditAccount {

    CBU cbu();

    Currency currency();

    Double balance();
}
