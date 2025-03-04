package com.es.prosacyte.web.demo.exception;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException() {
        super("Inventory not found.");
    }

    public InventoryNotFoundException(String message) {
        super(message);
    }

    public InventoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public InventoryNotFoundException(Long productId) {
        super("Inventory for product with ID " + productId + " not found.");
    }
}