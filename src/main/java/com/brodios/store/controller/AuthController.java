package com.brodios.store.controller;

import com.brodios.store.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Base path for Authentication Endpoints
public class AuthController {

    private final AuthService authService;

    // Constructor Injection
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Endpoint για Login (Επιστρέφει JWT)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password){
        String token = authService.login(username, password);
        return ResponseEntity.ok(token); // Επιστρέφει το JWT Token στο Front-end
    }

    // Endpoint για Registration (Εγγραφή)
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username, @RequestParam String email, @RequestParam String password){
        // Σημείωση: Στην πραγματικότητα, θα χρησιμοποιούσαμε DTO (Data Transfer Object) εδώ.
        String response = authService.register(username, email, password);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}