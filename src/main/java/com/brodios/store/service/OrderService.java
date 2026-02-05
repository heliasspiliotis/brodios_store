package com.brodios.store.service;

import com.brodios.store.domain.*;
import com.brodios.store.dto.*; // Παίρνει όλα τα DTOs
import com.brodios.store.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductVariantRepository variantRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository,
                        ProductVariantRepository variantRepository,
                        UserRepository userRepository,
                        OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.variantRepository = variantRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public void createOrder(OrderRequest request) {
        // Log για να δούμε τι φτάνει
        System.out.println("Service: Ξεκινάει η παραγγελία για " + request.getUsername());

        if (request.getItems() == null || request.getItems().isEmpty()) {
            System.err.println("ΣΦΑΛΜΑ: Η λίστα items είναι NULL!");
            throw new RuntimeException("Το καλάθι είναι άδειο (System Error: items is null)");
        }
        System.out.println("Service: Βρέθηκαν " + request.getItems().size() + " προϊόντα.");

        // 1. Βρίσκουμε τον χρήστη
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + request.getUsername()));

        // 2. Δημιουργούμε την Παραγγελία
        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());
        order.setShippingPhone(request.getShippingPhone());
        order.setTotalPrice(request.getTotalPrice());
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        // 3. Περνάμε τα προϊόντα
        for (OrderItemRequest itemRequest : request.getItems()) {
            ProductVariant variant = variantRepository.findById(itemRequest.getVariantId())
                    .orElseThrow(() -> new RuntimeException("Variant not found ID: " + itemRequest.getVariantId()));

            if (variant.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Δεν υπάρχει επαρκές απόθεμα: " + variant.getProduct().getName());
            }

            variant.setStockQuantity(variant.getStockQuantity() - itemRequest.getQuantity());
            variantRepository.save(variant);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setVariant(variant);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(variant.getProduct().getBasePrice());

            orderItemRepository.save(orderItem);
        }
        System.out.println("Service: Η παραγγελία ολοκληρώθηκε!");
    }
}