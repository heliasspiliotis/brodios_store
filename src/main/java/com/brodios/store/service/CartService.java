package com.brodios.store.service;

import com.brodios.store.domain.*;
import com.brodios.store.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductVariantRepository variantRepository;

    // 1. Βρες το καλάθι του χρήστη (ή φτιάξε νέο αν δεν έχει)
    public Cart getCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    // 2. Πρόσθεσε προϊόν στο καλάθι
    @Transactional
    public Cart addToCart(String username, Long variantId, int quantity) {
        Cart cart = getCart(username);
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Product Variant not found"));

        // Έλεγχος αν υπάρχει ήδη στο καλάθι
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getVariant().getId().equals(variantId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Αν υπάρχει, αύξησε την ποσότητα
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            // Αν δεν υπάρχει, φτιάξε νέο
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setVariant(variant);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        calculateTotalPrice(cart); // Υπολόγισε το νέο σύνολο
        return cartRepository.save(cart);
    }

    // 3. Υπολογισμός Συνόλου (Τιμή Προϊόντος * Ποσότητα)
    private void calculateTotalPrice(Cart cart) {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cart.getItems()) {
            // Παίρνουμε την τιμή (είναι ήδη BigDecimal)
            BigDecimal price = item.getVariant().getProduct().getBasePrice();

            // Μετατρέπουμε την ποσότητα σε BigDecimal για να κάνουμε πολλαπλασιασμό
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());

            // total = total + (price * quantity)
            total = total.add(price.multiply(quantity));
        }

        cart.setTotalPrice(total);
    }
}