package com.es.prosacyte.web.demo.repositories;

import com.es.prosacyte.web.demo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Product, UUID> {

    // Custom query to find products needing restock
    @Query("SELECT p FROM Product p WHERE p.inventory.isLowStock = true")
    List<Product> findProductsNeedingRestock();

    // Optimistic stock reservation
    @Modifying
    @Query("UPDATE Product p SET " +
            "p.inventory.quantityAvailable = p.inventory.quantityAvailable - :quantity, " +
            "p.inventory.quantityReserved = p.inventory.quantityReserved + :quantity " +
            "WHERE p.id = :productId AND p.inventory.quantityAvailable >= :quantity")
    int reserveStock(@Param("productId") UUID productId,
                     @Param("quantity") int quantity);

    // Bulk stock update
    @Modifying
    @Query("UPDATE Product p SET p.inventory.quantityAvailable = p.inventory.quantityAvailable + :quantity " +
            "WHERE p.id = :productId")
    int addStockToProduct(@Param("productId") UUID productId,
                          @Param("quantity") int quantity);

    // Inventory projection
    @Query("SELECT NEW com.es.prosacyte.web.demo.dto.InventoryStatusDTO(" +
            "p.id, p.sku, p.inventory.quantityAvailable, p.inventory.quantityReserved) " +
            "FROM Product p WHERE p.id = :productId")
    InventoryStatusDTO getInventoryStatus(@Param("productId") UUID productId);
}