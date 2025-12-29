package com.brodios.store.config;

import com.brodios.store.domain.Role;
import com.brodios.store.domain.enums.RoleName;
import com.brodios.store.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataInitializer {

    // Dependency Injection του RoleRepository
    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {

            // Λίστα ρόλων που πρέπει να υπάρχουν
            Arrays.asList(RoleName.ROLE_ADMIN, RoleName.ROLE_CUSTOMER).forEach(roleName -> {

                // Ελέγχουμε αν ο ρόλος υπάρχει ήδη
                if (roleRepository.findByName(roleName).isEmpty()) {
                    // Αν δεν υπάρχει, τον δημιουργούμε και τον αποθηκεύουμε
                    Role newRole = new Role(roleName);
                    roleRepository.save(newRole);
                    System.out.println("Role created: " + roleName.name());
                }
            });
        };
    }
}
