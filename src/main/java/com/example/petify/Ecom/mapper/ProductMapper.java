package com.example.petify.Ecom.mapper;

import com.example.petify.Ecom.dto.ProductDto;
import com.example.petify.domain.product.model.Product;
import com.example.petify.domain.product.repository.ProductRepository;
import com.example.petify.domain.user.repository.UserRepository;
import com.example.petify.exception.ResourceNotFoundException;

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
                .stock(product.getStock())
                .tags(tags)
                .images(product.getImageUrls())
                .build();
    }
}
