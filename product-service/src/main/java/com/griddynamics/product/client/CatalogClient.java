package com.griddynamics.product.client;

import com.griddynamics.product.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@FeignClient(name = "CATALOG-SERVICE")
public interface CatalogClient {

    @GetMapping("/product/{id}")
    @ResponseStatus(HttpStatus.OK)
    Product getProduct(@PathVariable String id);

    @GetMapping("products/{sku}")
    @ResponseStatus(HttpStatus.OK)
    List<Product> getProductBySku(@PathVariable String sku);

}
