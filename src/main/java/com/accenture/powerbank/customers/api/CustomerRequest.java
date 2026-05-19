package com.accenture.powerbank.customers.api;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerRequest(
        @NotBlank(message = "Customer names are required")
        @JsonProperty("names")
        @JsonAlias({"name", "firstName"})
        String names,

        @NotBlank(message = "Customer lastNames are required")
        @JsonProperty("lastNames")
        @JsonAlias({"lastName", "surname"})
        String lastNames,

        @NotBlank(message = "Customer CUIT is required")
        @Pattern(regexp = "^\\d{2}-?\\d{8}-?\\d$", message = "CUIT must have format 20-12345678-9 or 20123456789")
        @JsonProperty("cuit")
        @JsonAlias({"CUIT"})
        String cuit
) {
}
