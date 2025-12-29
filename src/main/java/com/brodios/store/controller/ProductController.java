package com.brodios.store.controller;

import com.brodios.store.domain.Category;
import com.brodios.store.domain.Product;
import com.brodios.store.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // PUBLIC: Όλοι μπορούν να δουν τα προϊόντα
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // PUBLIC: Όλοι μπορούν να δουν ένα συγκεκριμένο προϊόν
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // ADMIN ONLY: Μόνο διαχειριστές μπορούν να προσθέσουν κατηγορίες
    @PostMapping("/categories")
    @PreAuthorize("hasRole('ADMIN')") // <--- Authorization Check
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(productService.createCategory(category));
    }

    // ADMIN ONLY: Μόνο διαχειριστές μπορούν να προσθέσουν προϊόντα σε μια κατηγορία
    @PostMapping("/category/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')") // <--- Authorization Check
    public ResponseEntity<Product> createProduct(@PathVariable Long categoryId, @RequestBody Product product) {
        return ResponseEntity.ok(productService.createProduct(categoryId, product));
    }
}