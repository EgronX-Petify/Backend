package com.example.petify.Ecom.services;

import com.example.petify.Ecom.dto.CheckoutDto;
import com.example.petify.Ecom.dto.CheckoutResponse;
import com.example.petify.Ecom.dto.OrderDto;

public interface CheckoutService {
    public CheckoutResponse Checkout(Long userId, CheckoutDto data);
}
