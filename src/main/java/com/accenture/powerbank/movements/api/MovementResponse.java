package com.accenture.powerbank.movements.api;

import com.accenture.powerbank.common.domain.Currency;
import com.accenture.powerbank.movements.domain.MovementState;
import com.accenture.powerbank.movements.domain.MovementType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementResponse(
        Long id,
        BigDecimal amount,
        Currency currency,
        Long accountId,
        String productId,
        MovementState state,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String statusReason,
        MovementType movementType
) {
}
