package com.accenture.powerbank.customers.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<CustomerDocument, String> {

    boolean existsByCuit(String cuit);

    Optional<CustomerDocument> findByCuit(String cuit);
}
