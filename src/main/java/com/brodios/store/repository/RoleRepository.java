package com.brodios.store.repository;

import com.brodios.store.domain.Role;
import com.brodios.store.domain.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Εύρεση ρόλου με το όνομα
    Optional<Role> findByName(RoleName name);
}
