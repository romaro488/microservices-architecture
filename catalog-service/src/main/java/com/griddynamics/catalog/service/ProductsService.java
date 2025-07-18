package com.griddynamics.catalog.service;

import com.griddynamics.catalog.model.Product;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductsService {

    private volatile DataSnapshot dataSnapshot;

    @Getter
    private static class DataSnapshot {
        Map<String, Product> productMap;
        Map<String, List<Product>> skuMap;

        private DataSnapshot(Map<String, Product> productMap, Map<String, List<Product>> skuMap) {
            this.productMap = Collections.unmodifiableMap(productMap);
            this.skuMap = skuMap.entrySet().stream()
                    .collect(
                            Collectors.toUnmodifiableMap(
                                    Map.Entry::getKey,
                                    e -> Collections.unmodifiableList(e.getValue())
                            )
                    );
        }

        static DataSnapshot of(List<Product> allProducts) {
            Map<String, Product> productMap = new HashMap<>();
            Map<String, List<Product>> skuMap = new HashMap<>();
            for (Product product : allProducts) {
                Product oldProduct = productMap.put(product.uniqId(), product);
                if (oldProduct != null) {
                    log.error("Found products with duplicated uniq id {} and {}", oldProduct, product);
                }
                skuMap.computeIfAbsent(product.sku(), k -> new ArrayList<>()).add(product);
            }

            return new DataSnapshot(productMap, skuMap);
        }
    }

    public void update(List<Product> allProducts) {
        dataSnapshot = DataSnapshot.of(allProducts);
    }

    public Product getByUniqId(final String id) {
        if (dataSnapshot == null) {
            throw new IllegalArgumentException("Cache is empty.");
        }
        return dataSnapshot.getProductMap().get(id);
    }

    public List<Product> getBySku(final String sku) {
        if (dataSnapshot == null) {
            throw new IllegalArgumentException("Cache is empty.");
        }
        return dataSnapshot.getSkuMap().get(sku);
    }
}
