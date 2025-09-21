package com.example.petify.Ecom.services.impl;


import com.example.petify.Ecom.dto.ProductDto;
import com.example.petify.Ecom.dto.ProductFilter;
import com.example.petify.Ecom.mapper.ProductMapper;
import com.example.petify.Ecom.services.ProductService;
import com.example.petify.Ecom.specfication.ProductSpecification;
import com.example.petify.domain.product.model.Category;
import com.example.petify.domain.product.model.Product;
import com.example.petify.domain.product.model.Tag;
import com.example.petify.domain.product.repository.CategoryRepository;
import com.example.petify.domain.product.repository.ProductRepository;
import com.example.petify.domain.product.repository.TagRepository;
import com.example.petify.domain.profile.model.SPProfile;
import com.example.petify.domain.profile.repository.SPProfileRepository;
import com.example.petify.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
            product.setImageUrls(new ArrayList<>(productDto.getImages()));
        }
    }
}
