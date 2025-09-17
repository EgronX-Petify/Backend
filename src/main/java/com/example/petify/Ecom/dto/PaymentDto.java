package com.example.petify.Ecom.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class PaymentDto {
    @NotNull
    private Float amountInCents;

    @Builder.Default
    private String currency = "EGP";
    @Builder.Default
    private List<Integer> paymentMethods = new ArrayList<>();

    @NotNull
    private BillingData billingData;

    @Data
    @Builder
    public static class BillingData {
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
    }

    // webhook callback
    private String notificationUrl;
    // redirect to
    private String redirectionUrl;
}
