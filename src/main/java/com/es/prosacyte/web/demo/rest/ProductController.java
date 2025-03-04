package com.es.prosacyte.web.demo.rest;

import com.es.prosacyte.web.demo.entities.Product;
import com.es.prosacyte.web.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product request) {
        Product newProduct = productService.createProduct(request);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody Product request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<Product> publishProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.publishProduct(id));
    }

    @PostMapping("/{id}/stock")
    public ResponseEntity<Product> addStock(
            @PathVariable UUID id,
            @RequestParam int quantity) {
        return ResponseEntity.ok(productService.addInventory(id, quantity));
    }

    @PostMapping("/{id}/categories/{category}")
    public ResponseEntity<Product> addCategory(
            @PathVariable UUID id,
            @PathVariable String category) {
        return ResponseEntity.ok(productService.addToCategory(id, category));
    }

    @DeleteMapping("/{id}/categories/{category}")
    public ResponseEntity<Product> removeCategory(
            @PathVariable UUID id,
            @PathVariable String category) {
        return ResponseEntity.ok(productService.removeFromCategory(id, category));
    }
}