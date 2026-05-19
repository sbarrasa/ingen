package com.accenture.powerbank.products.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<ProductDocument, String> {

    List<ProductDocument> findByCustomerId(String customerId);
}
