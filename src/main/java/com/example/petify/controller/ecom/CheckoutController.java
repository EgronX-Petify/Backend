package com.example.petify.Ecom.controller;

import com.example.petify.Ecom.dto.CheckoutDto;
import com.example.petify.Ecom.services.CheckoutService;
import com.example.petify.config.security.UserInfoDetails;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @NotNull Authentication authentication,
            @NotNull @RequestBody CheckoutDto checkoutReq
            ) {
        UserInfoDetails user = (UserInfoDetails) authentication.getPrincipal();

        return ResponseEntity.status(HttpStatus.CREATED).body(
                checkoutService.Checkout(user.getId(), checkoutReq)
        );
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> Webhook() {
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
}
