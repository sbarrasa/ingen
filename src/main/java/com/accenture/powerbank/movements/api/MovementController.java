package com.accenture.powerbank.movements.api;

import com.accenture.powerbank.movements.service.MovementService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movements")
public class MovementController {

    private final MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @GetMapping
    public List<MovementResponse> getAll() {
        return movementService.getAll();
    }

    @GetMapping("/{id}")
    public MovementResponse getById(@PathVariable Long id) {
        return movementService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovementResponse create(@Valid @RequestBody CreateMovementRequest request) {
        return movementService.create(request);
    }
}
