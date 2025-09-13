package com.example.petify.Ecom.services;

import com.example.petify.Ecom.dto.ProductDto;
import com.example.petify.Ecom.dto.ProductFilter;

import java.util.List;

public interface ProductService {
    ProductDto getProduct(long id);

    List<ProductDto> getProducts(
            ProductFilter filter, int limit, int offset);

    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(ProductDto productDto);

    void deleteProduct(long id);
}
