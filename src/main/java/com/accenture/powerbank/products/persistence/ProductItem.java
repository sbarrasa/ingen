package com.accenture.powerbank.products.persistence;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductItem(
        @NotNull(message = "Product type is required")
        @JsonProperty("type")
        @JsonAlias({"productType"})
        ProductType type,

        @NotBlank(message = "Product name is required")
        @JsonProperty("name")
        String name,

        @JsonProperty("description")
        String description,

        @JsonProperty("active")
        Boolean active
) {
    public ProductItem {
        name = name == null ? null : name.trim();
        description = description == null || description.isBlank() ? null : description.trim();
        active = active == null ? Boolean.TRUE : active;
    }
}
