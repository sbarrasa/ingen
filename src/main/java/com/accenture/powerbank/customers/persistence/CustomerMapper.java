package com.accenture.powerbank.customers.persistence;

import com.accenture.powerbank.customers.api.CustomerRequest;
import com.accenture.powerbank.customers.api.CustomerResponse;

import java.time.Instant;

public final class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerDocument toDocument(CustomerRequest request) {
        Instant now = Instant.now();
        return new CustomerDocument(
                null,
                normalize(request.names()),
                normalize(request.lastNames()),
                normalizeCuit(request.cuit()),
                now,
                now
        );
    }

    public static void updateDocument(CustomerDocument target, CustomerRequest request) {
        target.setNames(normalize(request.names()));
        target.setLastNames(normalize(request.lastNames()));
        target.setCuit(normalizeCuit(request.cuit()));
        target.setUpdatedAt(Instant.now());
    }

    public static CustomerResponse toResponse(CustomerDocument document) {
        return new CustomerResponse(
                document.getId(),
                document.getNames(),
                document.getLastNames(),
                document.getCuit(),
                document.getCreatedAt(),
                document.getUpdatedAt()
        );
    }

    static String normalize(String value) {
        return value == null ? null : value.trim();
    }

    public static String normalizeCuit(String cuit) {
        return cuit == null ? null : cuit.trim();
    }
}
