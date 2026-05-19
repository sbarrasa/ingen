package com.accenture.powerbank.products.service;

import com.accenture.powerbank.api.domain.BadRequestException;
import com.accenture.powerbank.api.domain.ResourceNotFoundException;
import com.accenture.powerbank.customers.persistence.CustomerRepository;
import com.accenture.powerbank.products.api.ProductRequest;
import com.accenture.powerbank.products.api.ProductResponse;
import com.accenture.powerbank.products.persistence.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public ProductService(ProductRepository productRepository, CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    public ProductResponse findById(String id) {
        return productRepository.findById(id)
                .map(ProductMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Product document not found with id: " + id));
    }

    public List<ProductResponse> findByCustomerId(String customerId) {
        String normalizedCustomerId = ProductMapper.normalize(customerId);
        ensureCustomerExists(normalizedCustomerId);
        return productRepository.findByCustomerId(normalizedCustomerId)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    public ProductResponse create(ProductRequest request) {
        String customerId = ProductMapper.normalize(request.getCustomerId());
        ensureCustomerExists(customerId);

        List<ProductItem> normalizedProducts = validateAndNormalizeProducts(request);
        ProductDocument saved = productRepository.save(ProductMapper.toDocument(request, normalizedProducts));
        return ProductMapper.toResponse(saved);
    }

    public ProductResponse update(String id, ProductRequest request) {
        ProductDocument existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product document not found with id: " + id));

        String customerId = ProductMapper.normalize(request.getCustomerId());
        ensureCustomerExists(customerId);

        List<ProductItem> normalizedProducts = validateAndNormalizeProducts(request);
        ProductMapper.updateDocument(existing, request, normalizedProducts);
        return ProductMapper.toResponse(productRepository.save(existing));
    }

    public void delete(String id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product document not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private void ensureCustomerExists(String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new BadRequestException("Customer id is required");
        }
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }
    }

    private List<ProductItem> validateAndNormalizeProducts(ProductRequest request) {
        List<ProductItem> products = request.normalizedProducts();
        if (products == null || products.isEmpty()) {
            throw new BadRequestException("Product list is required. Use field 'products' with at least one item or send a single root-level field 'type'. Allowed values: " + ProductType.allowedValues());
        }

        products.forEach(product -> {
            if (product.type() == null) {
                throw new BadRequestException("Product type is required. Use field 'type' with " + ProductType.allowedValues());
            }
            if (product.name() == null || product.name().isBlank()) {
                throw new BadRequestException("Product name is required");
            }
        });

        return products;
    }
}
