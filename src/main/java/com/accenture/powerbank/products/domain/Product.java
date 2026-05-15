package com.accenture.powerbank.products.domain;

public sealed interface Product
        permits Account, Card, CreditCard, DebitCard, SavingAccount, CreditAccount  {

    default String id() {
        return switch (this) {
            case Card card -> card.cardNumber().value();
            case Account account -> account.cbu().value();
        };
    }

}
