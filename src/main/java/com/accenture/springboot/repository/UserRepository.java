package com.accenture.springboot.repository;

import com.accenture.springboot.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserDocument, Integer> {
}
