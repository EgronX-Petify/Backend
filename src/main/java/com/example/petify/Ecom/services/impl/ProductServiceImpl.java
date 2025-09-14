package com.example.petify.Ecom.services.impl;


import com.example.petify.Ecom.dto.ProductDto;
import com.example.petify.Ecom.dto.ProductFilter;
import com.example.petify.Ecom.mapper.ProductMapper;
import com.example.petify.Ecom.services.ProductService;
import com.example.petify.domain.product.model.Product;
import com.example.petify.domain.product.repository.ProductRepository;
import com.example.petify.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepo;

    @Override
    public ProductDto getProduct(long id) {
        Product product = productRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id " + id)
        );
        return ProductMapper.toDto(product);
    }

    @Override
    public List<ProductDto> getProducts(ProductFilter filter, int limit, int offset) {
        // TODO: filter this shit
        return List.of();
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = new Product();
        // TODO: Set attributes
        productRepo.save(product);
        return ProductMapper.toDto(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productRepo.findById(productDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id " + productDto.getId())
        );
        productRepo.save(product);
        // TODO: Update attributes
        return ProductMapper.toDto(product);
    }

    @Override
    public void deleteProduct(long id) {
        productRepo.deleteById(id);
    }
}
