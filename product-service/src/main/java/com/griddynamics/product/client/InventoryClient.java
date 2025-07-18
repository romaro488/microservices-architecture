package com.griddynamics.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

@FeignClient(value = "INVENTORY-SERVICE")
public interface InventoryClient {

    @GetMapping("available/{ids}")
    @ResponseStatus(HttpStatus.OK)
    Map<String, Boolean> loadInventory(@PathVariable final List<String> ids);
}
