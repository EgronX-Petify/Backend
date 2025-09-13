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
    private Integer id;

    @NotNull
    private Integer userId;

    @Builder.Default
    private List<CartProductDto> products = new ArrayList<>();

    public static class CartProductDto {
        @NotNull
        private Integer productId;

        @NotNull
        @Min(1)
        private Integer quantity;
    }
}


