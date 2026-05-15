package com.accenture.powerbank.movements.api;

import com.accenture.powerbank.common.domain.Currency;
import com.accenture.powerbank.movements.domain.MovementState;
import com.accenture.powerbank.movements.domain.MovementType;
import com.accenture.powerbank.movements.service.MovementNotFoundException;
import com.accenture.powerbank.movements.service.MovementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovementController.class)
class MovementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovementService movementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_ShouldReturn200() throws Exception {
        when(movementService.getAll()).thenReturn(List.of(
                movementResponse(1L, 10L, "SKU-ABC-001", MovementType.PAYMENT)
        ));

        mockMvc.perform(get("/movements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].accountId").value(10L))
                .andExpect(jsonPath("$[0].movementType").value("PAYMENT"));
    }

    @Test
    void getById_WhenExists_ShouldReturn200() throws Exception {
        when(movementService.getById(1L)).thenReturn(
                movementResponse(1L, 10L, "SKU-ABC-001", MovementType.PAYMENT)
        );

        mockMvc.perform(get("/movements/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productId").value("SKU-ABC-001"));
    }

    @Test
    void getById_WhenNotExists_ShouldReturn404() throws Exception {
        when(movementService.getById(99L)).thenThrow(new MovementNotFoundException(99L));

        mockMvc.perform(get("/movements/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movement 99 not found"));
    }

    @Test
    void create_WhenValid_ShouldReturn201() throws Exception {
        CreateMovementRequest request = new CreateMovementRequest(
                new BigDecimal("1500.50"),
                Currency.ARS,
                10L,
                "SKU-ABC-001",
                MovementType.PAYMENT
        );
        MovementResponse response = movementResponse(1L, 10L, "SKU-ABC-001", MovementType.PAYMENT);
        when(movementService.create(any())).thenReturn(response);

        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.state").value("CREATED"))
                .andExpect(jsonPath("$.movementType").value("PAYMENT"));
    }

    @Test
    void create_WhenInvalidSku_ShouldReturn400() throws Exception {
        CreateMovementRequest request = new CreateMovementRequest(
                new BigDecimal("1500.50"),
                Currency.ARS,
                10L,
                "sku invalido",
                MovementType.PAYMENT
        );

        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("productId must be a valid SKU"));
    }

    @Test
    void create_WhenTypeIsNull_ShouldReturn400() throws Exception {
        CreateMovementRequest request = new CreateMovementRequest(
                new BigDecimal("1500.50"),
                Currency.ARS,
                10L,
                "SKU-ABC-001",
                null
        );

        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("type must not be null"));

        verifyNoInteractions(movementService);
    }

    @Test
    void create_WhenTypeIsInvalid_ShouldReturn400() throws Exception {
        Map<String, Object> request = Map.of(
                "amount", new BigDecimal("1500.50"),
                "currency", "ARS",
                "accountId", 10,
                "productId", "SKU-ABC-001",
                "type", "INVALID_TYPE"
        );

        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "invalid type: INVALID_TYPE. Valid values: [PURCHASE, WITHDRAWAL, TRANSFER, PAYMENT, REFUND]"
                ));

        verifyNoInteractions(movementService);
    }

    @Test
    void create_WhenCurrencyIsInvalid_ShouldReturn400() throws Exception {
        Map<String, Object> request = Map.of(
                "amount", new BigDecimal("1500.50"),
                "currency", "INVALID_CURRENCY",
                "accountId", 10,
                "productId", "SKU-ABC-001",
                "type", "PAYMENT"
        );

        mockMvc.perform(post("/movements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "invalid currency: INVALID_CURRENCY. Valid values: [ARS, USD, EUR]"
                ));

        verifyNoInteractions(movementService);
    }

    private MovementResponse movementResponse(Long id, Long accountId, String productId, MovementType type) {
        return new MovementResponse(
                id,
                new BigDecimal("1500.50"),
                Currency.ARS,
                accountId,
                productId,
                MovementState.CREATED,
                LocalDateTime.of(2026, 5, 15, 12, 0),
                LocalDateTime.of(2026, 5, 15, 12, 0),
                "Movement created",
                type
        );
    }
}
