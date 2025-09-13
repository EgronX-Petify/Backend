package com.example.petify.Ecom.services.impl;


import com.example.petify.Ecom.dto.ProductDto;
import com.example.petify.Ecom.dto.ProductFilter;
import com.example.petify.Ecom.services.ProductService;
import com.example.petify.domain.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepo;

    @Override
    public ProductDto getProduct(long id) {
        return null;
    }

    @Override
    public List<ProductDto> getProducts(ProductFilter filter, int limit, int offset) {
        return List.of();
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        return null;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        return null;
    }

    @Override
    public void deleteProduct(long id) {

    }
}
