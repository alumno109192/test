package com.es.prosacyte.web.demo.service;

import com.es.prosacyte.web.demo.entities.Inventory;
import com.es.prosacyte.web.demo.exception.InventoryNotFoundException;
import com.es.prosacyte.web.demo.exception.ProductNotFoundException;
import com.es.prosacyte.web.demo.repository.InventoryRepository;
import com.es.prosacyte.web.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

// InventoryService.java (Updated)
@Service
@Transactional
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public InventoryService(InventoryRepository inventoryRepository,
                            ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public Inventory updateStock(Long productId, Integer quantity) {
        productRepository.findById(UUID.fromString(Long.toString(productId)))
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(productId));
        inventory.setQuantityAvailable(quantity);
        return inventoryRepository.save(inventory);
    }

    public Integer getStock(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .map(inventory -> inventory.getQuantityAvailable() + inventory.getQuantityReserved())
                .orElseThrow(() -> new InventoryNotFoundException(productId));
    }
}