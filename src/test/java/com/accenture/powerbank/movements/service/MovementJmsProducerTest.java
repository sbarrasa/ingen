package com.accenture.powerbank.movements.service;

import com.accenture.powerbank.common.domain.Currency;
import com.accenture.powerbank.movements.domain.Movement;
import com.accenture.powerbank.movements.domain.MovementState;
import com.accenture.powerbank.movements.domain.MovementType;
import com.accenture.powerbank.queue.events.MovementRequestedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.jms.core.JmsTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MovementJmsProducerTest {

    private final JmsTemplate jmsTemplate = mock(JmsTemplate.class);

    private final String destination = "movement.requests.queue";

    private final MovementJmsProducer movementJmsProducer = new MovementJmsProducer(jmsTemplate, destination);

    @Test
    void sendMovementRequested_ShouldCallJmsTemplateWithExpectedEvent() {
        Movement movement = movementEntity();
        ArgumentCaptor<MovementRequestedEvent> eventCaptor = ArgumentCaptor.forClass(MovementRequestedEvent.class);

        movementJmsProducer.sendMovementRequested(movement);

        verify(jmsTemplate).convertAndSend(eq(destination), eventCaptor.capture());

        MovementRequestedEvent event = eventCaptor.getValue();
        assertNotNull(event);
        assertEquals(MovementRequestedEvent.EVENT_TYPE, event.eventType());
        assertEquals(movement.getId(), event.movementId());
        assertEquals(movement.getAccountId(), event.accountId());
        assertEquals(movement.getProductId(), event.productId());
        assertEquals(movement.getMovementType(), event.type());
        assertEquals(movement.getAmount(), event.amount());
        assertEquals(movement.getCurrency(), event.currency());
        assertEquals(movement.getCreatedAt(), event.occurredAt());
        assertDoesNotThrow(() -> UUID.fromString(event.eventId()));
    }

    private Movement movementEntity() {
        Movement movement = new Movement();
        movement.setId(1L);
        movement.setAmount(new BigDecimal("1500.50"));
        movement.setCurrency(Currency.ARS);
        movement.setAccountId(10L);
        movement.setProductId("SKU-ABC-001");
        movement.setState(MovementState.CREATED);
        movement.setStatusReason("Movement created");
        movement.setMovementType(MovementType.PAYMENT);
        movement.setCreatedAt(LocalDateTime.of(2026, 5, 15, 12, 0));
        movement.setUpdatedAt(LocalDateTime.of(2026, 5, 15, 12, 0));
        return movement;
    }
}
