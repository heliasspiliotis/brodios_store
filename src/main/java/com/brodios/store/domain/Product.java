package com.brodios.store.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    // Σχέση Πολλά:1 (Many-to-One): Πολλά προϊόντα ανήκουν σε μία κατηγορία
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Σχέση 1:Πολλά (One-to-Many): Ένα προϊόν έχει πολλές παραλλαγές (Variants)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductVariant> variants;

    @Column(name = "base_price")
    private BigDecimal basePrice;
}