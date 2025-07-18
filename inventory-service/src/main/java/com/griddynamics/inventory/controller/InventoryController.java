package com.griddynamics.inventory.controller;

import com.griddynamics.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController("/api")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;
    private final Random rnd = new Random();

    @SneakyThrows
    @GetMapping("/available/{ids}")
    public Map<String, Boolean> available(@PathVariable List<String> ids) {
        Map<String, Boolean> result = new LinkedHashMap<>();
        for (String id : ids) {
            result.put(id, inventoryService.isAvailable(id));
        }
        int sleep = rnd.nextInt(2000);
        log.info("Load inventory for ids={} sleep={}", ids, sleep);
        Thread.sleep(sleep);
        return result;
    }

}
