package com.brodios.store.repository;


import com.brodios.store.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);

    // Μοναδικοποιεί το όνομα και το email
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}