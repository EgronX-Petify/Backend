package com.example.petify.mapper.ecom;

import com.example.petify.dto.ecom.ProductDto;
import com.example.petify.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {
    public static ProductDto toDto(Product product) {
        List<String> tags = new ArrayList<>();
        product.getTags().forEach(tag -> tags.add(tag.getName()));

        return ProductDto.builder()
                .id(product.getId())
                .sellerId(product.getSeller().getId())
                .name(product.getName())
                .description(product.getDescription())
                .notes(product.getNotes())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .final_price(product.getPrice() * (1 - product.getDiscount() / 100))
                .stock(product.getAvailableStock())
                .tags(tags)
                .images(product.getImages())
                .build();
    }
}
