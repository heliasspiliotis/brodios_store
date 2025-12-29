package com.brodios.store.controller;

import com.brodios.store.domain.Order;
import com.brodios.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // POST http://localhost:8080/api/orders/checkout
    @PostMapping("/checkout")
    public Order checkout(Principal principal) {
        return orderService.checkout(principal.getName());
    }
}
