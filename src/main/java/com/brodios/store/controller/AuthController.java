package com.brodios.store.controller;

import com.brodios.store.domain.User;
import com.brodios.store.dto.LoginRequest;
import com.brodios.store.dto.RegisterRequest;
import com.brodios.store.repository.UserRepository;
import com.brodios.store.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        // 1. Παίρνουμε το Token από το Service
        String token = authService.login(loginRequest);

        // 2. Βρίσκουμε τον χρήστη για να στείλουμε τα στοιχεία του στο Frontend
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Φτιάχνουμε την απάντηση (JSON)
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("address", user.getAddress());
        response.put("phone", user.getPhone());
        response.put("id", user.getId());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            // Προσπαθούμε να κάνουμε εγγραφή
            String response = authService.register(registerRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}