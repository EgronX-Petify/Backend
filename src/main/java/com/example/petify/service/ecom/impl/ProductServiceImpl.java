package com.example.petify.service.ecom.impl;


import com.example.petify.dto.ecom.ProductDto;
import com.example.petify.dto.ecom.ProductFilter;
import com.example.petify.mapper.ecom.ProductMapper;
import com.example.petify.service.ecom.ProductService;
import com.example.petify.specfication.ProductSpecification;
import com.example.petify.model.product.Category;
import com.example.petify.model.product.Product;
import com.example.petify.model.product.ProductImage;
import com.example.petify.model.product.Tag;
import com.example.petify.repository.product.CategoryRepository;
import com.example.petify.repository.product.ProductImageRepository;
import com.example.petify.repository.product.ProductRepository;
import com.example.petify.repository.product.TagRepository;
import com.example.petify.model.profile.SPProfile;
import com.example.petify.model.user.User;
import com.example.petify.repository.profile.SPProfileRepository;
import com.example.petify.service.auth.AuthenticatedUserService;
import com.example.petify.exception.FileStorageException;
import com.example.petify.exception.ResourceNotFoundException;
import com.example.petify.utils.FileStorageUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final ProductImageRepository productImageRepository;
    private final AuthenticatedUserService authenticatedUserService;

    @Override
    public ProductDto getProduct(long id) {
        Product product = productRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id " + id)
        );
        
        product.getImages().forEach(image -> {
            if (image.getFilePath() != null) {
                image.setData(FileStorageUtil.loadFile(image.getFilePath()));
            }
        });
        
        return ProductMapper.toDto(product);
    }

    @Override
    public Page<ProductDto> getProducts(ProductFilter filter, int limit, int offset) {
        return productRepo.findAll(ProductSpecification.filter(filter), PageRequest.of(offset, limit))
                .map(product -> {
                    product.getImages().forEach(image -> {
                        if (image.getFilePath() != null) {
                            image.setData(FileStorageUtil.loadFile(image.getFilePath()));
                        }
                    });
                    return ProductMapper.toDto(product);
                });
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

    // Product Image methods
    @Override
    public ProductImage addImage(Long productId, MultipartFile file) {
        User user = authenticatedUserService.getCurrentUser();
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        // Check if the current user is the owner of the product
        if(!product.getSeller().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not the owner of this product");
        }
        
        String filePath = FileStorageUtil.saveFile(file, "products");
        
        ProductImage image = ProductImage.builder()
                .contentType(file.getContentType())
                .name(file.getOriginalFilename())
                .filePath(filePath)
                .product(product)
                .build();

        return productImageRepository.save(image);

    
    }

    @Override
    public ProductImage getImageById(Long productId, Long imageId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        ProductImage image = productImageRepository.findById(imageId).orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));
        
        // Additional check to ensure the image belongs to the product
        if(!image.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Image does not belong to this product");
        }
        
        if (image.getFilePath() != null) {
            image.setData(FileStorageUtil.loadFile(image.getFilePath()));
        }
        
        return image;
    }

    @Override
    public List<ProductImage> getProductImages(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        List<ProductImage> images = productImageRepository.findByProductId(productId);
        
        images.forEach(image -> {
            if (image.getFilePath() != null) {
                image.setData(FileStorageUtil.loadFile(image.getFilePath()));
            }
        });
        
        return images;
    }

    @Override
    public void deleteProductImage(Long productId, Long imageId) {
        User user = authenticatedUserService.getCurrentUser();
        Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        if(!product.getSeller().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not the owner of this product");
        }
        
        ProductImage image = productImageRepository.findById(imageId).orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));
        
        // Additional check to ensure the image belongs to the product
        if(!image.getProduct().getId().equals(productId)) {
            throw new IllegalArgumentException("Image does not belong to this product");
        }
        
        if (image.getFilePath() != null) {
            FileStorageUtil.deleteFile(image.getFilePath());
        }
        
        productImageRepository.deleteById(imageId);
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
