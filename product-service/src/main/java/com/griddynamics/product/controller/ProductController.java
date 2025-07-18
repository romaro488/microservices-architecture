package com.griddynamics.product.controller;

import com.griddynamics.product.model.Product;
import com.griddynamics.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController("/api")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/product/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product productById(@PathVariable final String id) {
        return productService.getProduct(id);
    }

    @GetMapping("/products/{sku}")
    public List<Product> productsBySku(@PathVariable final String sku) {
        return productService.getBySku(sku);
    }


}
