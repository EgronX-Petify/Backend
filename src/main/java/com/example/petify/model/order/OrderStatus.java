package com.example.petify.domain.order.model;

public enum OrderStatus {
    PENDING_PAYMENT,
    PAID,
    IN_PROGRESS,
    DELIVERED,
    REFUNDED,
    CANCELLED
}