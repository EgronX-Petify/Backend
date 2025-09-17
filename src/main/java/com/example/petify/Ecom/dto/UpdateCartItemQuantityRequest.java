package com.example.petify.Ecom.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCartItemQuantityRequest {
    @Min(1)
    @NotNull
    private Integer quantity;
}
