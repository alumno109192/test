package com.es.prosacyte.web.demo.repository;

import com.es.prosacyte.web.demo.entities.Inventory;
import com.es.prosacyte.web.demo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(Long productId);

    // Custom query example (if needed)
    @Query("SELECT i FROM Inventory i WHERE i.quantity > :minQuantity")
    List<Inventory> findItemsWithStockGreaterThan(@Param("minQuantity") int minQuantity);
}