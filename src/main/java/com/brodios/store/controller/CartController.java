package com.brodios.store.controller;

import com.brodios.store.domain.Cart;
import com.brodios.store.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Δες το καλάθι μου (GET)
    @GetMapping
    public Cart getMyCart(Principal principal) {
        // Το Principal έχει το username του συνδεδεμένου χρήστη
        return cartService.getCart(principal.getName());
    }

    // Πρόσθεσε στο καλάθι (POST)
    // URL: /api/cart/add?variantId=1&quantity=2
    @PostMapping("/add")
    public Cart addToCart(@RequestParam Long variantId,
                          @RequestParam int quantity,
                          Principal principal) {
        return cartService.addToCart(principal.getName(), variantId, quantity);
    }
}
