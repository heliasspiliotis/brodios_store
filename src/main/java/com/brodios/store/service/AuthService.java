package com.brodios.store.service;

import com.brodios.store.domain.User;
import com.brodios.store.domain.Role;
import com.brodios.store.domain.enums.RoleName;
import com.brodios.store.repository.RoleRepository;
import com.brodios.store.repository.UserRepository;
import com.brodios.store.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // Constructor Injection για όλες τις εξαρτήσεις
    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Λειτουργία 1: Εγγραφή νέου χρήστη
    public String register(String username, String email, String password) {
        // Business Logic: Έλεγχος μοναδικότητας
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email is already registered!");
        }

        // 1. Δημιουργία νέου User
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Κωδικοποίηση password (BCrypt)

        // 2. Ορισμός default ρόλου: CUSTOMER
        Role customerRole = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Error: Customer Role is not defined."));
        user.setRoles(Set.of(customerRole));

        // 3. Αποθήκευση (Persistence Layer)
        userRepository.save(user);

        return "User registered successfully!";
    }

    // Λειτουργία 2: Login χρήστη
    public String login(String username, String password) {

        // 1. Πιστοποίηση: Ελέγχει τα credentials με βάση το CustomUserDetailsService
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // 2. Ορισμός της Authentication στο Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Δημιουργία και επιστροφή JWT
        return jwtTokenProvider.generateToken(authentication);
    }
}