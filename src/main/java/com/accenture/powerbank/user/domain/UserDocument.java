package com.accenture.powerbank.user.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "users")
public record UserDocument(
        @Id Integer id,
        String name,
        LocalDate registrationDate
) {
}
