package com.example.petify.dto.ecom;

import com.example.petify.model.product.ProductImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;


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
    private Set<ProductImage> images;
}