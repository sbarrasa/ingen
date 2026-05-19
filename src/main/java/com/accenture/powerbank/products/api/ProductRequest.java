package com.accenture.powerbank.products.api;

import com.accenture.powerbank.products.persistence.ProductItem;
import com.accenture.powerbank.products.persistence.ProductType;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class ProductRequest {

    @NotBlank(message = "Customer id is required")
    @JsonProperty("customerId")
    private String customerId;

    @Valid
    @JsonProperty("products")
    private List<ProductItem> products;

    // Backward-compatible fields. They allow old Bruno requests with a single product at root level.
    @JsonProperty("type")
    @JsonAlias({"productType"})
    private ProductType type;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("active")
    private Boolean active;

    public ProductRequest() {
    }

    public ProductRequest(String customerId, List<ProductItem> products, ProductType type, String name, String description, Boolean active) {
        this.customerId = customerId;
        this.products = products;
        this.type = type;
        this.name = name;
        this.description = description;
        this.active = active;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<ProductItem> getProducts() {
        return products;
    }

    public void setProducts(List<ProductItem> products) {
        this.products = products;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @JsonIgnore
    public List<ProductItem> normalizedProducts() {
        if (products != null && !products.isEmpty()) {
            return products;
        }
        if (type != null) {
            return List.of(new ProductItem(type, name, description, active));
        }
        return List.of();
    }
}
