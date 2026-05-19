package com.accenture.powerbank.customers.service;

import com.accenture.powerbank.api.domain.BadRequestException;
import com.accenture.powerbank.api.domain.ResourceNotFoundException;
import com.accenture.powerbank.customers.api.CustomerRequest;
import com.accenture.powerbank.customers.api.CustomerResponse;
import com.accenture.powerbank.customers.persistence.CustomerDocument;
import com.accenture.powerbank.customers.persistence.CustomerMapper;
import com.accenture.powerbank.customers.persistence.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponse> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    public CustomerResponse findById(String id) {
        return customerRepository.findById(id)
                .map(CustomerMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    public CustomerResponse findByCuit(String cuit) {
        return customerRepository.findByCuit(CustomerMapper.normalizeCuit(cuit))
                .map(CustomerMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with CUIT: " + cuit));
    }

    public CustomerResponse create(CustomerRequest request) {
        String cuit = CustomerMapper.normalizeCuit(request.cuit());
        if (customerRepository.existsByCuit(cuit)) {
            throw new BadRequestException("Customer already exists with CUIT: " + cuit);
        }
        CustomerDocument saved = customerRepository.save(CustomerMapper.toDocument(request));
        return CustomerMapper.toResponse(saved);
    }

    public CustomerResponse update(String id, CustomerRequest request) {
        CustomerDocument existing = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        String newCuit = CustomerMapper.normalizeCuit(request.cuit());
        customerRepository.findByCuit(newCuit)
                .filter(customer -> !customer.getId().equals(id))
                .ifPresent(customer -> {
                    throw new BadRequestException("Another customer already exists with CUIT: " + newCuit);
                });

        CustomerMapper.updateDocument(existing, request);
        return CustomerMapper.toResponse(customerRepository.save(existing));
    }

    public void delete(String id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
}
