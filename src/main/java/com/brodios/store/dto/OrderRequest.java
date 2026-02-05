package com.brodios.store.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderRequest {
    private String username;
    private String shippingAddress;
    private String shippingPhone;
    private BigDecimal totalPrice;
    private List<OrderItemRequest> items; // Η λίστα που έρχεται null

    // --- GETTERS ---
    public String getUsername() { return username; }
    public String getShippingAddress() { return shippingAddress; }
    public String getShippingPhone() { return shippingPhone; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public List<OrderItemRequest> getItems() { return items; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public void setShippingPhone(String shippingPhone) { this.shippingPhone = shippingPhone; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }


    public void setItems(List<OrderItemRequest> items) { this.items = items; }
}