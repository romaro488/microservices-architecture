package com.griddynamics.product.service;

import com.griddynamics.product.client.CatalogClient;
import com.griddynamics.product.client.InventoryClient;
import com.griddynamics.product.exceptions.ProductNotFoundException;
import com.griddynamics.product.model.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final CatalogClient catalogClient;
    private final InventoryClient inventoryClient;

    @CircuitBreaker(name = "defaultProduct", fallbackMethod = "fallbackProduct")
    public Product getProduct(final String id) {
        Product product = catalogClient.getProduct(id);
        if (product == null) {
            throw new ProductNotFoundException("product with id {} not found");
        }
        final Map<String, Boolean> inventoryMap = inventoryClient.loadInventory(Collections.singletonList(product.getUniqId()));
        Boolean isProductAvailable = inventoryMap.get(product.getUniqId());
        log.info("FINISH");
        return Boolean.TRUE.equals(isProductAvailable) ? product : null;
    }


    @CircuitBreaker(name = "defaultProducts", fallbackMethod = "fallbackLoadInventory")
    public List<Product> getBySku(final String sku) {
        final List<Product> products = catalogClient.getProductBySku(sku);
        if (CollectionUtils.isEmpty(products)) {
            throwErrorIfEmpty(new ProductNotFoundException(String.format("Products with ids %s not found", sku)));
        }
        final List<String> ids = products.stream()
                .filter(Objects::nonNull)
                .map(Product::getUniqId)
                .filter(Objects::nonNull)
                .toList();

        final Map<String, Boolean> availabilityMap = inventoryClient.loadInventory(ids);

        List<Product> result = new ArrayList<>(products.size());
        for (Product product : products) {
            if (Boolean.TRUE.equals(availabilityMap.get(product.getUniqId()))) {
                result.add(product);
            }
        }
        log.info("FINISH");
        return result;
    }


    public Product fallbackProduct(Throwable throwable) {
        log.error("Error occurred during getting available product by uniqId {}", throwable.getMessage());
        return Product.builder().uniqId("Cashed product id").sku("Cashed product sku").build();
    }


    public Map<String, Boolean> fallbackLoadInventory(List<String> ids, Throwable throwable) {
        log.error("Could not retrieve inventory. ids={} exception={}", ids, throwable.getMessage());
        return Map.of("123", false);
    }

    private static void throwErrorIfEmpty(RuntimeException exception) {
        log.error(exception.getMessage());
        throw exception;
    }
}
