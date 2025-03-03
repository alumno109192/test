package com.es.prosacyte.web.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Inventory as a Value Object within the Product aggregate
 * Encapsulates inventory-related behavior and state
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Required by JPA
public class Inventory {

    @Column(name = "quantity_available", nullable = false)
    private int quantityAvailable;

    @Column(name = "quantity_reserved", nullable = false)
    private int quantityReserved;

    @Column(name = "reorder_threshold")
    private Integer reorderThreshold;

    public Inventory(int initialQuantity) {
        this.quantityAvailable = initialQuantity;
        this.quantityReserved = 0;
    }

    /**
     * Add stock to available inventory
     */
    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantityAvailable += quantity;
    }

    /**
     * Reserve inventory if available
     * @return true if reservation was successful
     */
    public boolean reserve(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if (quantityAvailable >= quantity) {
            quantityAvailable -= quantity;
            quantityReserved += quantity;
            return true;
        }
        return false;
    }

    /**
     * Release previously reserved inventory back to available
     */
    public void releaseReservation(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if (quantity > quantityReserved) {
            throw new IllegalArgumentException("Cannot release more than reserved quantity");
        }

        quantityReserved -= quantity;
        quantityAvailable += quantity;
    }

    /**
     * Confirm reservation (e.g., after order is completed)
     */
    public void confirmReservation(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if (quantity > quantityReserved) {
            throw new IllegalArgumentException("Cannot confirm more than reserved quantity");
        }

        quantityReserved -= quantity;
    }

    /**
     * Set reorder threshold for inventory management
     */
    public void setReorderThreshold(int threshold) {
        if (threshold < 0) {
            throw new IllegalArgumentException("Threshold cannot be negative");
        }
        this.reorderThreshold = threshold;
    }

    /**
     * Check if inventory is below reorder threshold
     */
    public boolean isLowStock() {
        return reorderThreshold != null && quantityAvailable <= reorderThreshold;
    }
}