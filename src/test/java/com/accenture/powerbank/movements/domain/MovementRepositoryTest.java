package com.accenture.powerbank.movements.domain;

import com.accenture.powerbank.common.domain.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class MovementRepositoryTest {

    @Autowired
    private MovementRepository movementRepository;

    @Test
    void save_ShouldPersistMovementWithAuditFields() {
        Movement movement = new Movement();
        movement.setAmount(new BigDecimal("999.99"));
        movement.setCurrency(Currency.USD);
        movement.setAccountId(22L);
        movement.setProductId("SKU-TEST-001");
        movement.setState(MovementState.CREATED);
        movement.setStatusReason("Movement created");
        movement.setMovementType(MovementType.PAYMENT);

        Movement persisted = movementRepository.saveAndFlush(movement);

        assertNotNull(persisted.getId());
        assertNotNull(persisted.getCreatedAt());
        assertNotNull(persisted.getUpdatedAt());
        assertEquals(MovementState.CREATED, persisted.getState());
    }
}
