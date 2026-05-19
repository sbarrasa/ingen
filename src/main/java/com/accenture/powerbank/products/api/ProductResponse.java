package com.accenture.powerbank.products.api;

import com.accenture.powerbank.products.persistence.ProductItem;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResponse(
        @JsonProperty("id")
        String id,

        @JsonProperty("customerId")
        String customerId,

        @JsonProperty("products")
        List<ProductItem> products,

        @JsonProperty("createdAt")
        Instant createdAt,

        @JsonProperty("updatedAt")
        Instant updatedAt
) {
}
