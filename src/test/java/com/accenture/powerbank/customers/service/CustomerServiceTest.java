package com.accenture.powerbank.customers.service;

import com.accenture.powerbank.customers.api.CustomerRequest;
import com.accenture.powerbank.customers.api.CustomerResponse;
import com.accenture.powerbank.customers.persistence.CustomerDocument;
import com.accenture.powerbank.customers.persistence.CustomerRepository;
import com.accenture.powerbank.customers.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Service Tests")
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerRepository);
    }

    @Test
    @DisplayName("Should create customer when CUIT is valid and does not exist")
    void shouldCreateCustomerWhenCuitIsValidAndDoesNotExist() {
        CustomerRequest request = new CustomerRequest(
                "Sergio",
                "Rozenberg",
                "20-12345678-6"
        );

        CustomerDocument savedDocument = new CustomerDocument(
                "customer-001",
                "Sergio",
                "Rozenberg",
                "20123456786"
        );

        when(customerRepository.existsByCuit("20123456786"))
                .thenReturn(false);

        when(customerRepository.save(any(CustomerDocument.class)))
                .thenReturn(savedDocument);

        CustomerResponse response = customerService.create(request);

        assertNotNull(response);
        assertEquals("customer-001", response.id());
        assertEquals("Sergio", response.names());
        assertEquals("Rozenberg", response.lastNames());
        assertEquals("20123456786", response.cuit());

        verify(customerRepository, times(1)).existsByCuit("20123456786");
        verify(customerRepository, times(1)).save(any(CustomerDocument.class));
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    @DisplayName("Should reject duplicated CUIT")
    void shouldRejectDuplicatedCuit() {
        CustomerRequest request = new CustomerRequest(
                "Sergio",
                "Rozenberg",
                "20-12345678-6"
        );

        when(customerRepository.existsByCuit("20123456786"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> customerService.create(request)
        );

        assertEquals("Customer already exists with CUIT: 20123456786", exception.getMessage());

        verify(customerRepository, times(1)).existsByCuit("20123456786");
        verify(customerRepository, never()).save(any(CustomerDocument.class));
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    @DisplayName("Should find customer by id")
    void shouldFindCustomerById() {
        CustomerDocument document = new CustomerDocument(
                "customer-001",
                "Sergio",
                "Rozenberg",
                "20123456786"
        );

        when(customerRepository.findById("customer-001"))
                .thenReturn(Optional.of(document));

        CustomerResponse response = customerService.findById("customer-001");

        assertNotNull(response);
        assertEquals("customer-001", response.id());
        assertEquals("Sergio", response.names());
        assertEquals("Rozenberg", response.lastNames());
        assertEquals("20123456786", response.cuit());

        verify(customerRepository, times(1)).findById("customer-001");

    }

    @Test
    @DisplayName("Should throw exception when customer does not exist")
    void shouldThrowExceptionWhenCustomerDoesNotExist() {
        when(customerRepository.findById("customer-999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> customerService.findById("customer-999")
        );

        assertEquals("Customer not found with id: customer-999", exception.getMessage());

        verify(customerRepository, times(1)).findById("customer-999");
        verifyNoMoreInteractions(customerRepository);
    }
}



