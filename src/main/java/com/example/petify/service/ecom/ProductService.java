package com.example.petify.service.ecom;

import com.example.petify.dto.ecom.ProductDto;
import com.example.petify.dto.ecom.ProductFilter;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductDto getProduct(long id);

    Page<ProductDto> getProducts(
            ProductFilter filter, int limit, int offset);

    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(ProductDto productDto);

    void deleteProduct(long id);
}
