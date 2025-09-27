package com.example.petify.domain.order.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class PaymentTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String transactionId;

    @Column(unique = true)
    private String transactionOrderId;

    private String currency;

    private Integer amountInCents;

    private String status;

    private Boolean isVoided;
    private Boolean isRefunded;
    private Boolean isCapture;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(updatable = false)
    private LocalDateTime updatedAt;
}
