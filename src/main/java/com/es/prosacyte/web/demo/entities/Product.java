package com.es.prosacyte.web.demo.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Product entity representing a core domain concept in the product catalog.
 * Follows DDD principles with a rich domain model pattern.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Required by JPA, but protected to enforce factory methods
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @ElementCollection
    @CollectionTable(name = "product_categories", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "category")
    private Set<String> categories = new HashSet<>();

    @Version
    private Long version;

    // Value object for tracking inventory
    @Embedded
    private Inventory inventory;

    /**
     * Factory method for creating new products
     */
    public static Product create(String sku, String name, String description, BigDecimal price, int initialStock) {
        Product product = new Product();
        product.sku = sku;
        product.name = name;
        product.description = description;
        product.price = price;
        product.status = ProductStatus.DRAFT;
        product.inventory = new Inventory(new Product(), initialStock);

        return product;
    }

    /**
     * Domain business method to publish a product (make it available)
     */
    public void publish() {
        if (this.inventory.getQuantityAvailable() <= 0) {
            throw new IllegalStateException("Cannot publish a product with no inventory");
        }

        if (this.price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Product price must be greater than zero");
        }

        this.status = ProductStatus.AVAILABLE;
    }

    /**
     * Domain business method to add product to a category
     */
    public void addToCategory(String category) {
        this.categories.add(category);
    }

    /**
     * Domain business method to remove product from a category
     */
    public void removeFromCategory(String category) {
        this.categories.remove(category);
    }

    /**
     * Domain business method to update product price
     */
    public void updatePrice(BigDecimal newPrice) {
        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        this.price = newPrice;
    }

    /**
     * Domain business method to stock inventory
     */
    public void stockInventory(int quantity) {
        this.inventory.addStock(quantity);
    }

    /**
     * Domain business method to reserve inventory
     */
    public boolean reserveInventory(int quantity) {
        return this.inventory.reserve(quantity);
    }

    /**
     * Domain business method to discontinue a product
     */
    public void discontinue() {
        this.status = ProductStatus.DISCONTINUED;
    }

    /**
     * Product status in its lifecycle
     */
    public enum ProductStatus {
        DRAFT,
        AVAILABLE,
        OUT_OF_STOCK,
        DISCONTINUED
    }
}