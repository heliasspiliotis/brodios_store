package com.brodios.store.service;

import com.brodios.store.domain.*;
import com.brodios.store.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService; // Χρησιμοποιούμε το CartService για να βρούμε το καλάθι
    @Autowired
    private ProductVariantRepository variantRepository;

    @Transactional // Σημαντικό: Ή γίνονται όλα ή τίποτα!
    public Order checkout(String username) {
        // 1. Βρες το καλάθι του χρήστη
        Cart cart = cartService.getCart(username);

        // 2. Έλεγχος αν είναι άδειο
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Το καλάθι είναι άδειο!");
        }

        // 3. Δημιούργησε την Παραγγελία
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus("COMPLETED");

        // 4. Μετέτρεψε τα CartItems σε OrderItems & Μείωσε το Stock
        for (CartItem cartItem : cart.getItems()) {
            ProductVariant variant = cartItem.getVariant();
            int quantity = cartItem.getQuantity();

            // Έλεγχος Stock
            if (variant.getStockQuantity() < quantity) {
                throw new RuntimeException("Δεν υπάρχει επαρκές απόθεμα για το προϊόν: " + variant.getProduct().getName());
            }

            // Μείωση Stock
            variant.setStockQuantity(variant.getStockQuantity() - quantity);
            variantRepository.save(variant);

            // Δημιουργία OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setVariant(variant);
            orderItem.setQuantity(quantity);
            orderItem.setPriceAtPurchase(variant.getProduct().getBasePrice()); // Κρατάμε την τιμή που είχε τώρα

            order.getItems().add(orderItem);
        }

        // 5. Αποθήκευσε την Παραγγελία
        Order savedOrder = orderRepository.save(order);

        // 6. Άδειασε το Καλάθι
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        return savedOrder;
    }
}
