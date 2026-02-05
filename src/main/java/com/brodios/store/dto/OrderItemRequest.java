package com.brodios.store.dto;

public class OrderItemRequest {
    private Long variantId;
    private int quantity;

    // --- GETTERS ---
    public Long getVariantId() { return variantId; }
    public int getQuantity() { return quantity; }

    // --- SETTERS ---
    public void setVariantId(Long variantId) { this.variantId = variantId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}