package com.brodios.store.repository;

import com.brodios.store.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId); // Βρες τις παραγγελίες του χρήστη
}