package com.accenture.powerbank.movements.service;

import com.accenture.powerbank.common.domain.Currency;
import com.accenture.powerbank.movements.api.CreateMovementRequest;
import com.accenture.powerbank.movements.api.MovementResponse;
import com.accenture.powerbank.movements.domain.Movement;
import com.accenture.powerbank.movements.domain.MovementRepository;
import com.accenture.powerbank.movements.domain.MovementState;
import com.accenture.powerbank.movements.domain.MovementType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovementServiceTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private MovementJmsProducer movementJmsProducer;

    private MovementService movementService;

    @BeforeEach
    void setUp() {
        movementService = new MovementService(movementRepository, movementJmsProducer);
    }

    @Test
    void getAll_ShouldReturnMappedResponses() {
        when(movementRepository.findAll()).thenReturn(List.of(
                movementEntity(1L, 5L, "SKU-EUR-100", MovementType.REFUND)
        ));

        List<MovementResponse> responses = movementService.getAll();

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).id());
        assertEquals("SKU-EUR-100", responses.get(0).productId());
    }

    @Test
    void getById_WhenExists_ShouldReturnMappedResponse() {
        when(movementRepository.findById(1L)).thenReturn(Optional.of(
                movementEntity(1L, 5L, "SKU-EUR-100", MovementType.REFUND)
        ));

        MovementResponse response = movementService.getById(1L);

        assertEquals(1L, response.id());
        assertEquals(MovementType.REFUND, response.movementType());
    }

    @Test
    void getById_WhenMissing_ShouldThrowNotFound() {
        when(movementRepository.findById(99L)).thenReturn(Optional.empty());

        MovementNotFoundException exception = assertThrows(
                MovementNotFoundException.class,
                () -> movementService.getById(99L)
        );

        assertEquals("Movement 99 not found", exception.getMessage());
    }

    @Test
    void create_ShouldPersistMovementWithDefaults() {
        CreateMovementRequest request = new CreateMovementRequest(
                new BigDecimal("100.00"),
                Currency.EUR,
                5L,
                "SKU-EUR-100",
                MovementType.REFUND
        );

        Movement persisted = movementEntity(1L, request.accountId(), request.productId(), request.type());
        persisted.setAmount(request.amount());
        persisted.setCurrency(request.currency());

        when(movementRepository.save(any(Movement.class))).thenReturn(persisted);

        MovementResponse response = movementService.create(request);

        assertNotNull(response.id());
        assertEquals(MovementState.CREATED, response.state());
        assertEquals("Movement created", response.statusReason());
        assertEquals(MovementType.REFUND, response.movementType());
        verify(movementJmsProducer).sendMovementRequested(persisted);
    }

    private Movement movementEntity(Long id, Long accountId, String productId, MovementType type) {
        Movement movement = new Movement();
        movement.setId(id);
        movement.setAmount(new BigDecimal("100.00"));
        movement.setCurrency(Currency.EUR);
        movement.setAccountId(accountId);
        movement.setProductId(productId);
        movement.setState(MovementState.CREATED);
        movement.setStatusReason("Movement created");
        movement.setMovementType(type);
        movement.setCreatedAt(LocalDateTime.of(2026, 5, 15, 12, 0));
        movement.setUpdatedAt(LocalDateTime.of(2026, 5, 15, 12, 0));
        return movement;
    }
}
