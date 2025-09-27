package com.example.petify.service.ecom;

import com.example.petify.dto.ecom.CheckoutDto;
import com.example.petify.dto.ecom.CheckoutResponse;

public interface CheckoutService {
    public CheckoutResponse Checkout(Long userId, CheckoutDto data);
}
