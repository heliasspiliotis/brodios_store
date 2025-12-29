package com.brodios.store.service;

import com.brodios.store.domain.Category;
import com.brodios.store.domain.Product;
import com.brodios.store.repository.CategoryRepository;
import com.brodios.store.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // 1. Δημιουργία Προϊόντος (Σύνδεση με Category ΚΑΙ Variants)
    public Product createProduct(Long categoryId, Product product) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setCategory(category);

        if (product.getVariants() != null) {
            for (com.brodios.store.domain.ProductVariant variant : product.getVariants()) {
                variant.setProduct(product); // Συνδέουμε το παιδί με τον γονέα
            }
        }

        return productRepository.save(product);
    }

    // 2. Εύρεση όλων των προϊόντων
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 3. Εύρεση προϊόντος με βάση το ID
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // 4. Δημιουργία Κατηγορίας (Βοηθητικό για να έχουμε δεδομένα)
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
}