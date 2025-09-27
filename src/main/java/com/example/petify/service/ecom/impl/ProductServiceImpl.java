package com.example.petify.service.ecom.impl;


import com.example.petify.dto.ecom.ProductDto;
import com.example.petify.dto.ecom.ProductFilter;
import com.example.petify.mapper.ecom.ProductMapper;
import com.example.petify.service.ecom.ProductService;
import com.example.petify.specfication.ProductSpecification;
import com.example.petify.model.product.Category;
import com.example.petify.model.product.Product;
import com.example.petify.model.product.Tag;
import com.example.petify.repository.product.CategoryRepository;
import com.example.petify.repository.product.ProductRepository;
import com.example.petify.repository.product.TagRepository;
import com.example.petify.model.profile.SPProfile;
import com.example.petify.repository.profile.SPProfileRepository;
import com.example.petify.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepo;
    private final SPProfileRepository spProfileRepo;
    private final CategoryRepository categoryRepo;
    private final TagRepository tagRepo;

    @Override
    public ProductDto getProduct(long id) {
        Product product = productRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id " + id)
        );
        return ProductMapper.toDto(product);
    }

    @Override
    public Page<ProductDto> getProducts(ProductFilter filter, int limit, int offset) {
        return productRepo.findAll(ProductSpecification.filter(filter), PageRequest.of(offset, limit))
                .map(ProductMapper::toDto);
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product product = new Product();
        this.setProductAttr(product, productDto);
        productRepo.save(product);
        return ProductMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productRepo.findById(productDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id " + productDto.getId())
        );
        this.setProductAttr(product, productDto);
        productRepo.save(product);
        return ProductMapper.toDto(product);
    }

    @Override
    public void deleteProduct(long id) {
        productRepo.deleteById(id);
    }


    @Transactional
    protected void setProductAttr(Product product, ProductDto productDto) {
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setNotes(productDto.getNotes());
        product.setDiscount(productDto.getDiscount() != null ? productDto.getDiscount() : 0.0);
        product.setStock(productDto.getStock());

        SPProfile spProfile = spProfileRepo.findById(productDto.getSellerId()).orElseThrow(
                () -> new ResourceNotFoundException("Seller (service provider profile) not found with id " + productDto.getSellerId())
        );
        product.setSeller(spProfile);


        if (productDto.getCategory() != null && !productDto.getCategory().isBlank()) {
            Category category = categoryRepo.findByName(productDto.getCategory())
                    .orElseGet(() -> categoryRepo.save(
                            Category.builder()
                                    .name(productDto.getCategory())
                                    .build()
                    ));
            product.setCategory(category);
        }

        if (productDto.getTags() != null && !productDto.getTags().isEmpty()) {
            Set<Tag> tags = productDto.getTags().stream()
                    .map(tagName -> tagRepo.findByName(tagName)
                            .orElseGet(()->  tagRepo.save(
                                        Tag.builder()
                                                .name(tagName)
                                                .build()
                            ))
                    )
                    .collect(Collectors.toSet());
            product.setTags(tags);
        }

        if (productDto.getImages() != null) {
            product.setImages(productDto.getImages());
        }
    }
}
