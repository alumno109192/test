package com.es.prosacyte.web.demo.interfaz;

import com.es.prosacyte.web.demo.entities.Inventory;
import com.es.prosacyte.web.demo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Product domain entity
 * Following DDD patterns with Spring Data JPA
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    Optional<Inventory> findBySku(String sku);

    List<Inventory> findByStatus(Inventory status);

    @Query("SELECT p FROM Inventory p JOIN p.categories c WHERE c = :category")
    List<Inventory> findByCategory(@Param("category") String category);

    List<Inventory> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    @Query("SELECT p FROM Inventory p WHERE p.inventory.quantityAvailable <= p.inventory.reorderThreshold")
    List<Inventory> findProductsNeedingRestock();
}