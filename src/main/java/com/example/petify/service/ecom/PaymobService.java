package com.example.petify.service.ecom;

import com.example.petify.dto.ecom.PaymentDto;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public interface PaymobService {
    String authenticate();
    Map generateIntentionPayment(PaymentDto payment);
    Map getTransactionByTrnxOrderId(Long trnxOrderId);
    void refund(String transactionId, @NotNull Integer amountInCents);
}
