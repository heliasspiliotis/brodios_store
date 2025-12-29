package com.brodios.store.repository;

import com.brodios.store.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Μπορούμε να προσθέσουμε custom queries αν χρειαστεί
}