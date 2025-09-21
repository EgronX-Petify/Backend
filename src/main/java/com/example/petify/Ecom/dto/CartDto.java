package com.example.petify.Ecom.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CartDto {
    @NotNull
    private Long id;

    @NotNull
    private Long userId;

    @Builder.Default
    private List<CartProductDto> products = new ArrayList<>();

    @Data
    @Builder
    public static class CartProductDto {
        @NotNull
        private Long productId;

        @NotNull
        @Min(1)
        private Integer quantity;
    }
}


