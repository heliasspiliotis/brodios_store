package com.brodios.store.config;

import com.brodios.store.domain.Category;
import com.brodios.store.domain.Product;
import com.brodios.store.domain.ProductVariant;
import com.brodios.store.domain.Role;
import com.brodios.store.domain.enums.RoleName;
import com.brodios.store.repository.CategoryRepository;
import com.brodios.store.repository.ProductRepository;
import com.brodios.store.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(RoleRepository roleRepository,
                               CategoryRepository categoryRepository,
                               ProductRepository productRepository) {
        return args -> {
            // 1. Αρχικοποίηση Ρόλων
            if (roleRepository.count() == 0) {
                roleRepository.save(new Role(RoleName.ROLE_CUSTOMER));
                roleRepository.save(new Role(RoleName.ROLE_ADMIN));
            }

            // 2. Αρχικοποίηση Προϊόντων (αν δεν υπάρχουν)
            if (productRepository.count() == 0) {
                System.out.println("--- Ξεκινάει η εισαγωγή προϊόντων ---");

                // --- Κατηγορία: Φανέλες ---
                Category catJerseys = createCategoryIfNotFound(categoryRepository, "Φανέλες");

                createProduct(productRepository, catJerseys,
                        "Εντός έδρας εμφάνιση (μαύρη)",
                        "Πρώτη εμφάνιση ομάδος, μαύρου χρώματος με λευκές λεπτομέρειες",
                        new BigDecimal("50.00"),
                        "First_Kit.jpeg");

                createProduct(productRepository, catJerseys,
                        "Εκτός έδρας εμφάνιση (λευκή)",
                        "Δεύτερη εμφάνιση ομάδος, σε λευκό χρώμα με γκρι και μαύρες λεπτομέρειες",
                        new BigDecimal("50.00"),
                        "Second_Kit.jpeg");

                // --- Κατηγορία: Ζακέτες ---
                Category catJackets = createCategoryIfNotFound(categoryRepository, "Ζακέτες");

                createProduct(productRepository, catJackets,
                        "Ζακέτα προπόνησης",
                        "Λεπτή ζακέτα προπόνησης σε μαύρο χρώμα με λευκές λεπτομέρειες, χωρίς κουκούλα",
                        new BigDecimal("30.00"),
                        "Jacket.jpeg");

                createProduct(productRepository, catJackets,
                        "Ζακέτα προπόνησης με κουκούλα",
                        "Λεπτή ζακέτα προπόνησης σε μαύρο χρώμα με λευκές λεπτομέρειες, με ενσωματωμένη κουκούλα",
                        new BigDecimal("35.00"),
                        "Jacket.jpeg");

                // --- Κατηγορία: Μπουφάν ---
                Category catCoats = createCategoryIfNotFound(categoryRepository, "Μπουφάν");

                createProduct(productRepository, catCoats,
                        "Μπουφάν κοντό",
                        "Μπουφάν ομάδος μαύρου χρώματος",
                        new BigDecimal("60.00"),
                        "HoodedJacket_Short.jpeg");

                createProduct(productRepository, catCoats,
                        "Μπουφάν μακρύ",
                        "Μπουφάν ομάδος μαύρου χρώματος, μακρύ",
                        new BigDecimal("80.00"),
                        "HoodedJacket_Long.jpeg");

                // --- Κατηγορία: Προπόνηση ---
                Category catTraining = createCategoryIfNotFound(categoryRepository, "Προπόνηση");

                createProduct(productRepository, catTraining,
                        "Παντελονάκι εντός έδρας εμφάνισης",
                        "Παντελονάκι μαύρου χρώματος με άσπρες λεπτομέρειες",
                        new BigDecimal("20.00"),
                        "Shorts_First_Kit.jpeg");

                createProduct(productRepository, catTraining,
                        "Αμάνυκο (άσπρο - μαύρο)",
                        "Αμάνικο μπλουζάκι σε δύο χρώματα, λευκό και μαύρο",
                        new BigDecimal("25.00"),
                        "Training_Kit.jpeg");

                System.out.println("--- Η εισαγωγή προϊόντων ολοκληρώθηκε! ---");
            }
        };
    }

    // Βοηθητική μέθοδος για δημιουργία Κατηγορίας
    private Category createCategoryIfNotFound(CategoryRepository repo, String name) {
        return repo.findByName(name).orElseGet(() -> {
            Category newCat = new Category();
            newCat.setName(name);
            return repo.save(newCat);
        });
    }

    // Βοηθητική μέθοδος για δημιουργία Προϊόντος με Variants
    private void createProduct(ProductRepository repo, Category category, String name, String desc, BigDecimal price, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(desc);
        product.setBasePrice(price);
        // Πλήρες URL
        product.setImageUrl("http://localhost:8080/images/" + imageUrl);
        product.setCategory(category);

        // Δημιουργία Variants (Μεγέθη)
        List<ProductVariant> variants = new ArrayList<>();
        String[] sizes = {"Small", "Medium", "Large", "XL"};

        for (String size : sizes) {
            ProductVariant variant = new ProductVariant();
            variant.setProduct(product);
            variant.setSize(size);
            variant.setColor("Default");
            variant.setStockQuantity(50); // 50 κομμάτια απόθεμα
            variants.add(variant);
        }

        product.setVariants(variants);
        repo.save(product);
    }
}
