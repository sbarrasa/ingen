package com.accenture.powerbank.customers.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerResponse(
        @JsonProperty("id")
        String id,

        @JsonProperty("names")
        String names,

        @JsonProperty("lastNames")
        String lastNames,

        @JsonProperty("cuit")
        String cuit

) {
}
