package com.accenture.powerbank.movements.api;

import com.accenture.powerbank.common.domain.Currency;
import com.accenture.powerbank.movements.domain.MovementType;
import com.accenture.powerbank.movements.service.MovementNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice(assignableTypes = MovementController.class)
public class MovementsExceptionHandler {

    @ExceptionHandler(MovementNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleMovementNotFound(MovementNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .orElse("Invalid request body");

        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadableMessage(HttpMessageNotReadableException exception) {
        Throwable cause = exception.getCause();

        if (cause instanceof InvalidFormatException invalidFormatException
                && !invalidFormatException.getPath().isEmpty()) {
            String field = invalidFormatException.getPath().get(0).getFieldName();

            if ("type".equals(field)) {
                return buildResponse(HttpStatus.BAD_REQUEST, invalidEnumMessage(
                        "type",
                        invalidFormatException.getValue(),
                        MovementType.values()
                ));
            }

            if ("currency".equals(field)) {
                return buildResponse(HttpStatus.BAD_REQUEST, invalidEnumMessage(
                        "currency",
                        invalidFormatException.getValue(),
                        Currency.values()
                ));
            }
        }

        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request body");
    }

    private String invalidEnumMessage(String field, Object invalidValue, Enum<?>[] validValues) {
        String values = Arrays.stream(validValues)
                .map(Enum::name)
                .collect(Collectors.joining(", "));

        return "invalid %s: %s. Valid values: [%s]".formatted(field, invalidValue, values);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", status.value(),
                        "error", status.getReasonPhrase(),
                        "message", message
                ));
    }
}
