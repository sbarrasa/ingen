package com.accenture.powerbank.products.domain;

import com.accenture.powerbank.common.domain.Currency;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProductTest {
    static List<Product> products = List.of(
           new CreditCard(
                    new CardNumber("1111222233334444"),
                    Brand.VISA,
                    "Gold",
                    123,
                    LocalDate.now(),
                    LocalDate.now().plusYears(5),
                    100000.0
            ),
            new CreditCard(
                    new CardNumber("5555666677778888"),
                    Brand.MC,
                    "Black",
                    456,
                    LocalDate.now(),
                    LocalDate.now().plusYears(5),
                    200000.0
            ),
            new DebitCard(
                    new CardNumber("9999000011112222"),
                    Brand.MC,
                    789,
                    LocalDate.now(),
                    LocalDate.now().plusYears(5)
            ),
            new SavingAccount(
                    new CBU("1234567890123456789012"),
                    Currency.ARS,
                    50000.0
            ),
            new CreditAccount(
                    new CBU("9999999999999999999999"),
                    Currency.ARS,
                    1000.0,
                    10_000_000.0
            )
    );



    @Test
    void getProductId(){

        Product product = products.getFirst();
        var id = product.id();

        assertEquals("1111222233334444", id);


    }

    @Test
    void product() {

        for (Product product : products) {

            switch (product) {

                case CreditCard creditCard -> {
                    assertEquals(16, creditCard.cardNumber().value().length());
                    assertTrue(creditCard.limit() > 0);
                }

                case DebitCard debitCard -> {
                    assertEquals(16, debitCard.cardNumber().value().length());
                    assertTrue(debitCard.cvv() > 0);
                }

                case SavingAccount savingAccount -> {
                    assertEquals(22, savingAccount.cbu().value().length());
                    assertEquals(Currency.ARS, savingAccount.currency());
                }

                case CreditAccount creditAccount -> {
                    assertEquals(22, creditAccount.cbu().value().length());
                    assertEquals(Currency.ARS, creditAccount.currency());
                    assertTrue(creditAccount.limit() > 0);
                }


            }
        }
    }
}
