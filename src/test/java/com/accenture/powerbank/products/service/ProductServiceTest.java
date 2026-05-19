package com.accenture.powerbank.products.service;

import com.accenture.powerbank.customers.persistence.CustomerRepository;
import com.accenture.powerbank.products.api.ProductRequest;
import com.accenture.powerbank.products.api.ProductResponse;
import com.accenture.powerbank.products.persistence.ProductDocument;
import com.accenture.powerbank.products.persistence.ProductRepository;
import com.accenture.powerbank.products.persistence.ProductType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Product Service Tests")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, customerRepository);
    }

    @Test
    @DisplayName("Should create product when customer exists")
    void shouldCreateProductWhenCustomerExists() {
        ProductRequest request = new ProductRequest(
                "customer-001",
                ProductType.DEBIT_CARD,
                "Main Debit Card"
        );

        ProductDocument savedDocument = new ProductDocument(
                "product-001",
                "customer-001",
                ProductType.DEBIT_CARD,
                "Main Debit Card"
        );

        when(customerRepository.existsById("customer-001"))
                .thenReturn(true);

        when(productRepository.save(any(ProductDocument.class)))
                .thenReturn(savedDocument);

        ProductResponse response = productService.create(request);

        assertNotNull(response);
        assertEquals("product-001", response.id());
        assertEquals("customer-001", response.customerId());
        assertEquals(ProductType.DEBIT_CARD, response.products().getFirst().type());
        assertEquals("Main Debit Card", response.products().getFirst().name());

        verify(customerRepository, times(1)).existsById("customer-001");
        verify(productRepository, times(1)).save(any(ProductDocument.class));
        verifyNoMoreInteractions(customerRepository);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("Should reject product creation when customer does not exist")
    void shouldRejectProductCreationWhenCustomerDoesNotExist() {
        ProductRequest request = new ProductRequest(
                "customer-999",
                ProductType.DEBIT_CARD,
                "Main Debit Card"
        );

        when(customerRepository.existsById("customer-999"))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.create(request)
        );

        assertEquals("Customer not found with id: customer-999", exception.getMessage());

        verify(customerRepository, times(1)).existsById("customer-999");
        verify(productRepository, never()).save(any(ProductDocument.class));
        verifyNoMoreInteractions(customerRepository);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    @DisplayName("Should find product by id")
    void shouldFindProductById() {
        ProductDocument document = new ProductDocument(
                "product-001",
                "customer-001",
                ProductType.CREDIT_CARD,
                "Gold Credit Card"
        );

        when(productRepository.findById("product-001"))
                .thenReturn(Optional.of(document));

        ProductResponse response = productService.findById("product-001");

        assertNotNull(response);
        assertEquals("product-001", response.id());
        assertEquals("customer-001", response.customerId());
        assertEquals(ProductType.CREDIT_CARD, response.products().getFirst().type());
        assertEquals("Gold Credit Card", response.products().getFirst().name());

        verify(productRepository, times(1)).findById("product-001");
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(customerRepository);
    }

    @Test
    @DisplayName("Should throw exception when product does not exist")
    void shouldThrowExceptionWhenProductDoesNotExist() {
        when(productRepository.findById("product-999"))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.findById("product-999")
        );

        assertEquals("Product not found with id: product-999", exception.getMessage());

        verify(productRepository, times(1)).findById("product-999");
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(customerRepository);
    }

    @Test
    @DisplayName("Should delete product by id")
    void shouldDeleteProductById() {
        when(productRepository.existsById("product-001"))
                .thenReturn(true);

        productService.delete("product-001");

        verify(productRepository, times(1)).existsById("product-001");
        verify(productRepository, times(1)).deleteById("product-001");
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(customerRepository);
    }

    @Test
    @DisplayName("Should not delete product when id does not exist")
    void shouldNotDeleteProductWhenIdDoesNotExist() {
        when(productRepository.existsById("product-999"))
                .thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.delete("product-999")
        );

        assertEquals("Product not found with id: product-999", exception.getMessage());

        verify(productRepository, times(1)).existsById("product-999");
        verify(productRepository, never()).deleteById(anyString());
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(customerRepository);
    }
}