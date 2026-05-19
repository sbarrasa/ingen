package com.accenture.powerbank.products.persistence;

import com.accenture.powerbank.products.api.ProductRequest;
import com.accenture.powerbank.products.api.ProductResponse;

import java.time.Instant;
import java.util.List;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static ProductDocument toDocument(ProductRequest request, List<ProductItem> normalizedProducts) {
        Instant now = Instant.now();
        return new ProductDocument(
                null,
                normalize(request.getCustomerId()),
                normalizedProducts,
                now,
                now
        );
    }

    public static void updateDocument(ProductDocument target, ProductRequest request, List<ProductItem> normalizedProducts) {
        target.setCustomerId(normalize(request.getCustomerId()));
        target.setProducts(normalizedProducts);
        target.setUpdatedAt(Instant.now());
    }

    public static ProductResponse toResponse(ProductDocument document) {
        return new ProductResponse(
                document.getId(),
                document.getCustomerId(),
                document.getProducts(),
                document.getCreatedAt(),
                document.getUpdatedAt()
        );
    }

    public static String normalize(String value) {
        return value == null ? null : value.trim();
    }
}
