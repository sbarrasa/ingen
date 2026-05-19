package com.accenture.powerbank.movements.api;

import com.accenture.powerbank.common.domain.Currency;
import com.accenture.powerbank.movements.domain.MovementType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record CreateMovementRequest(
        @NotNull(message = "must not be null")
        @DecimalMin(value = "0.01", message = "must be greater than 0")
        BigDecimal amount,

        @NotNull(message = "must not be null")
        Currency currency,

        @NotNull(message = "must not be null")
        Long accountId,

        @NotBlank(message = "must not be blank")
        @Pattern(
                regexp = "^[A-Z0-9]+(?:[-_][A-Z0-9]+)*$",
                message = "must be a valid SKU"
        )
        String productId,

        @NotNull(message = "must not be null")
        MovementType type
) {
}
