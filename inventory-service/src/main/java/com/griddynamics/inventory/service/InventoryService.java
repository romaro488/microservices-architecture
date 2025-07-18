package com.griddynamics.inventory.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InventoryService {

    private final Map<String, Boolean> inventory = new ConcurrentHashMap<>();
    private final Random rnd = new Random();

    public boolean isAvailable(String id) {
        return inventory.computeIfAbsent(id, k -> rnd.nextBoolean());
    }

}
