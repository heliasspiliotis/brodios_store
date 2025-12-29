package com.brodios.store.repository;

import com.brodios.store.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId); // Βρες το καλάθι του τάδε χρήστη
}