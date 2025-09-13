package com.example.petify.Ecom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
public class ProductDto {
    private Long id;

    @NotBlank
    private Long sellerId;

    @NotBlank
    private String name;

    private String description;

    private String notes;

    @NotNull
    private Double price;

    private Double discount;

    @NotNull
    private Double final_price;

    @NotNull
    private Integer stock;

    private String category;

    private List<String> tags;
    private List<String> images;
}