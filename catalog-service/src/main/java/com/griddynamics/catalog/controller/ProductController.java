package com.griddynamics.catalog.controller;

import com.griddynamics.catalog.model.Product;
import com.griddynamics.catalog.service.ProductsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductsService productService;

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") final String id) {
        return productService.getByUniqId(id);
    }

    @GetMapping("/products/{sku}")
    public List<Product> getProductBySku(@PathVariable("sku") final String sku) {
        return productService.getBySku(sku);
    }

}
