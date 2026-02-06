package com.brodios.store.controller;

import com.brodios.store.dto.OrderRequest;
import com.brodios.store.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // Χρειαζόμαστε αυτό το import

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/create")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request, Principal principal) {
        try {
            String realUsername = principal.getName();
            request.setUsername(realUsername);

            System.out.println("Δημιουργία παραγγελίας για τον χρήστη: " + realUsername);

            orderService.createOrder(request);

            return ResponseEntity.ok("Η παραγγελία καταχωρήθηκε επιτυχώς!");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("ΣΦΑΛΜΑ Server: " + e.getMessage());
        }
    }
}