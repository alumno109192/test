package com.es.prosacyte.web.demo.entities;

import com.es.prosacyte.web.demo.exception.InvalidReleaseQuantityException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

// Inventory.java
@Entity
@Table(name = "inventories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "product_id", unique = true)
    private Product product;

    @Column(nullable = false)
    private int quantityAvailable;

    @Column(nullable = false)
    private int quantityReserved;

    private int reorderThreshold;

    @Version
    private Long version;

    public Inventory(Product product, int initialQuantity) {
        this.product = product;
        this.quantityAvailable = initialQuantity;
        this.quantityReserved = 0;
    }

    // Domain Methods
    public void addStock(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        this.quantityAvailable += quantity;
    }

    public boolean reserve(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if (quantity > quantityAvailable) {
            return false;
        }

        this.quantityAvailable -= quantity;
        this.quantityReserved += quantity;
        return true;
    }

    public void releaseReservation(int quantity) {
        if (quantity > quantityReserved) throw new InvalidReleaseQuantityException();
        this.quantityReserved -= quantity;
        this.quantityAvailable += quantity;
    }

    public boolean needsRestock() {
        return quantityAvailable <= reorderThreshold;
    }
}