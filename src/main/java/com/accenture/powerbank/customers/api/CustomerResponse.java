package com.accenture.powerbank.customers.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerResponse(
        @JsonProperty("id")
        String id,

        @JsonProperty("names")
        String names,

        @JsonProperty("lastNames")
        String lastNames,

        @JsonProperty("cuit")
        String cuit,

        @JsonProperty("createdAt")
        Instant createdAt,

        @JsonProperty("updatedAt")
        Instant updatedAt
) {
}
