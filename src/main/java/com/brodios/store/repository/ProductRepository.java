package com.brodios.store.repository;

import com.brodios.store.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom query για εύρεση προϊόντων βάσει ID κατηγορίας
    List<Product> findByCategoryId(Long categoryId);
}