package com.example.petify.Ecom.services;

import com.example.petify.Ecom.dto.PaymentDto;
import com.example.petify.domain.order.model.Order;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public interface PaymobService {
    String authenticate();
    Map generateIntentionPayment(PaymentDto payment);
    Map getTransactionByTrnxOrderId(Long trnxOrderId);
    void refund(String transactionId, @NotNull Integer amountInCents);
}
