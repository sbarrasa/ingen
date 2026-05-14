package com.accenture.powerbank.products.domain;

public sealed interface Product
        permits Account, Card, CreditAccount, CreditCard, DebitCard, SavingAccount {

    default String id() {
        return switch (this) {
            case Card card -> card.cardNumber().value();
            case Account account -> account.cbu().value();
        };
    }

}
