package com.accenture.powerbank.queue.events;

import com.accenture.powerbank.common.domain.Currency;
import com.accenture.powerbank.movements.domain.MovementType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementRequestedEvent(
        String eventId,
        String eventType,
        Long movementId,
        Long accountId,
        String productId,
        MovementType type,
        BigDecimal amount,
        Currency currency,
        LocalDateTime occurredAt
) implements Serializable {

    public static final String EVENT_TYPE = "MOVEMENT_REQUESTED";
}
