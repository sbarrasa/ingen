package com.accenture.powerbank.customers.api;

import com.accenture.powerbank.customers.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@DisplayName("Customer Controller API Tests")
class CustomerApiTest {

    private static final String BASE_URL = "/api/customers";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerService customerService;

    @Test
    @DisplayName("POST /api/customers should create customer when request is valid")
     void shouldCreateCustomerWhenRequestIsValid() throws Exception {
        CustomerResponse response = new CustomerResponse(
                "customer-001",
                "Sergio",
                "Rozenberg",
            "20-12345678-6"
        );

        when(customerService.create(argThat(validCustomerRequest())))
                .thenReturn(response);

        Map<String, Object> requestBody = Map.of(
                "names", "Sergio",
                "lastNames", "Rozenberg",
                "cuit", "20-12345678-6"
        );

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("customer-001"))
                .andExpect(jsonPath("$.names").value("Sergio"))
                .andExpect(jsonPath("$.lastNames").value("Rozenberg"))
                .andExpect(jsonPath("$.cuit").value("20123456786"));

        verify(customerService, times(1)).create(any(CustomerRequest.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("POST /api/customers should return 400 when CUIT is invalid")
    void shouldReturnBadRequestWhenCuitIsInvalid() throws Exception {
        Map<String, Object> requestBody = Map.of(
                "names", "Sergio",
                "lastNames", "Rozenberg",
                "cuit", "20-12345678-9"
        );

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerService);
    }

    @Test
    @DisplayName("POST /api/customers should return 400 when names is missing")
    void shouldReturnBadRequestWhenNamesIsMissing() throws Exception {
        Map<String, Object> requestBody = Map.of(
                "lastNames", "Rozenberg",
                "cuit", "20-12345678-6"
        );

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(customerService);
    }

    @Test
    @DisplayName("GET /api/customers/{id} should return customer")
    void shouldFindCustomerById() throws Exception {
        CustomerResponse response = new CustomerResponse(
                "customer-001",
                "Sergio",
                "Rozenberg",
             "20-12345678-6"
        );

        when(customerService.findById("customer-001"))
                .thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/{id}", "customer-001")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("customer-001"))
                .andExpect(jsonPath("$.names").value("Sergio"))
                .andExpect(jsonPath("$.lastNames").value("Rozenberg"))
                .andExpect(jsonPath("$.cuit").value("20123456786"));

        verify(customerService, times(1)).findById("customer-001");
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("GET /api/customers should return all customers")
    void shouldFindAllCustomers() throws Exception {
        List<CustomerResponse> customers = List.of(
                new CustomerResponse(
                        "customer-001",
                        "Sergio",
                        "Rozenberg",
                    "20-12345678-6"
                ),
                new CustomerResponse(
                        "customer-002",
                        "Maria",
                        "Gomez",
                        "27-12345678-4"
                )
        );

        when(customerService.findAll())
                .thenReturn(customers);

        mockMvc.perform(get(BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("customer-001"))
                .andExpect(jsonPath("$[0].cuit").value("20123456786"))
                .andExpect(jsonPath("$[1].id").value("customer-002"))
                .andExpect(jsonPath("$[1].cuit").value("27123456784"));

        verify(customerService, times(1)).findAll();
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("PUT /api/customers/{id} should update customer")
    void shouldUpdateCustomer() throws Exception {
        CustomerResponse response = new CustomerResponse(
                "customer-001",
                "Sergio Updated",
                "Rozenberg",
             "20-12345678-6"
        );

        when(customerService.update(eq("customer-001"), any(CustomerRequest.class)))
                .thenReturn(response);

        Map<String, Object> requestBody = Map.of(
                "names", "Sergio Updated",
                "lastNames", "Rozenberg",
                "cuit", "20-12345678-6"
        );

        mockMvc.perform(put(BASE_URL + "/{id}", "customer-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("customer-001"))
                .andExpect(jsonPath("$.names").value("Sergio Updated"))
                .andExpect(jsonPath("$.lastNames").value("Rozenberg"))
                .andExpect(jsonPath("$.cuit").value("20123456786"));

        verify(customerService, times(1)).update(eq("customer-001"), any(CustomerRequest.class));
        verifyNoMoreInteractions(customerService);
    }

    @Test
    @DisplayName("DELETE /api/customers/{id} should delete customer")
    void shouldDeleteCustomer() throws Exception {
        doNothing().when(customerService).delete("customer-001");

        mockMvc.perform(delete(BASE_URL + "/{id}", "customer-001"))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).delete("customer-001");
        verifyNoMoreInteractions(customerService);
    }

    private static ArgumentMatcher<CustomerRequest> validCustomerRequest() {
        return request ->
                request != null
                        && "Sergio".equals(request.names())
                        && "Rozenberg".equals(request.lastNames())
                        && request.cuit() != null
                        && "20123456786".equals(request.cuit());
    }
}