package com.example.petify.dto.ecom;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckoutDto {
    @NotNull
    private String address;
    @NotNull
    private String contactInfo;
    private String notes;
}
