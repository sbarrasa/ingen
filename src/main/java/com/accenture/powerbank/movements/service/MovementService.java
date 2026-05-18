package com.accenture.powerbank.movements.service;

import com.accenture.powerbank.movements.api.CreateMovementRequest;
import com.accenture.powerbank.movements.api.MovementResponse;
import com.accenture.powerbank.movements.domain.Movement;
import com.accenture.powerbank.movements.domain.MovementRepository;
import com.accenture.powerbank.movements.domain.MovementState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovementService {

    private static final String DEFAULT_STATUS_REASON = "Movement created";

    private final MovementRepository movementRepository;
    private final MovementJmsProducer movementJmsProducer;

    public MovementService(MovementRepository movementRepository, MovementJmsProducer movementJmsProducer) {
        this.movementRepository = movementRepository;
        this.movementJmsProducer = movementJmsProducer;
    }

    public List<MovementResponse> getAll() {
        return movementRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MovementResponse getById(Long id) {
        return movementRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new MovementNotFoundException(id));
    }

    @Transactional
    public MovementResponse create(CreateMovementRequest request) {
        Movement movement = new Movement();
        movement.setAmount(request.amount());
        movement.setCurrency(request.currency());
        movement.setAccountId(request.accountId());
        movement.setCreatedAt(LocalDateTime.now());
        movement.setUpdatedAt(LocalDateTime.now());
        movement.setProductId(request.productId());
        movement.setState(MovementState.CREATED);
        movement.setStatusReason(DEFAULT_STATUS_REASON);
        movement.setMovementType(request.type());

        Movement savedMovement = movementRepository.save(movement);
        movementJmsProducer.sendMovementRequested(savedMovement);
        return toResponse(savedMovement);
    }

    private MovementResponse toResponse(Movement movement) {
        return new MovementResponse(
                movement.getId(),
                movement.getAmount(),
                movement.getCurrency(),
                movement.getAccountId(),
                movement.getProductId(),
                movement.getState(),
                movement.getCreatedAt(),
                movement.getUpdatedAt(),
                movement.getStatusReason(),
                movement.getMovementType()
        );
    }
}
