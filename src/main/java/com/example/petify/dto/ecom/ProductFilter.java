package com.example.petify.dto.ecom;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductFilter {
    private String name;
    private String category;
    private List<String> tags;
}
