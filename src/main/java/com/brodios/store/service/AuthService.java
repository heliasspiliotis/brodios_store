package com.brodios.store.service;

import com.brodios.store.domain.User;
import com.brodios.store.domain.Role;
import com.brodios.store.domain.enums.RoleName;
import com.brodios.store.dto.LoginRequest;
import com.brodios.store.dto.RegisterRequest;
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

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // ΑΛΛΑΓΗ 1: Δέχεσαι RegisterRequest (DTO) αντί για σκέτα Strings
    // για να πάρεις και τη διεύθυνση/τηλέφωνο.
    public String register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // ---  Αποθήκευση Διεύθυνσης & Τηλεφώνου ---
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());


        Role customerRole = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Error: Customer Role is not defined."));
        user.setRoles(Set.of(customerRole));

        userRepository.save(user);

        return "User registered successfully!";
    }

    // ΑΛΛΑΓΗ 2: Δέχεσαι LoginRequest
    public String login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.generateToken(authentication);
    }
}