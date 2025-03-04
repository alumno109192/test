package com.es.prosacyte.web.demo.service;

import com.es.prosacyte.web.demo.entities.Inventory;
import com.es.prosacyte.web.demo.entities.Product;
import com.es.prosacyte.web.demo.exception.InventoryNotFoundException;
import com.es.prosacyte.web.demo.exception.ProductNotFoundException;
import com.es.prosacyte.web.demo.repository.InventoryRepository;
import com.es.prosacyte.web.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public ProductService(ProductRepository productRepository,
                          InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Product createProduct(Product product) {
        Product savedProduct = productRepository.save(product);
        inventoryRepository.save(new Inventory(savedProduct, 0));
        return savedProduct;
    }

    public Product updateProduct(UUID productId, Product productDetails) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.valueOf(productId)));
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        return productRepository.save(product);
    }

    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.valueOf(productId)));
        Inventory inventory = inventoryRepository.findByProductId(Long.getLong(productId.toString()))
                .orElseThrow(() -> new InventoryNotFoundException(String.valueOf(productId)));
        productRepository.delete(product);
        inventoryRepository.delete(inventory);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(UUID id) {
        return productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException(Long.getLong(id.toString())));
    }

    public Product publishProduct(UUID id) {
        return productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException(Long.getLong(id.toString())));
    }

    public Product addInventory(UUID id, int quantity) {
        return null;
    }

    public Product addToCategory(UUID id, String category) {
        return null;
    }

    public Product removeFromCategory(UUID id, String category) {
        return null;
    }
}
