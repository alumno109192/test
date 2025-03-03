package com.es.prosacyte.web.demo.interfaz;

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
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findBySku(String sku);

    List<Product> findByStatus(Product.ProductStatus status);

    //@Query("SELECT p FROM Product p JOIN p.categories c WHERE c = :category")
    //List<Product> findByCategory(@Param("category") String category);

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

   // @Query("SELECT p FROM Product p WHERE p.inventory.quantityAvailable <= p.inventory.reorderThreshold")
    //List<Product> findProductsNeedingRestock();
}