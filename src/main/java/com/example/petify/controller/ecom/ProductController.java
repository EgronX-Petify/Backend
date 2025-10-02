package com.example.petify.controller.ecom;


import com.example.petify.dto.ecom.ProductDto;
import com.example.petify.dto.ecom.ProductFilter;
import com.example.petify.model.product.ProductImage;
import com.example.petify.service.ecom.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable long id) {
        ProductDto product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    // Get paginated list of products
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ProductFilter filter = ProductFilter.builder()
                .name(name)
                .category(category)
                .tags(tags)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(
                productService.getProducts(filter, size, page)
        );
    }

    // Create new product
    @PostMapping
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto createdProduct = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // Update existing product
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable long id,
            @RequestBody ProductDto productDto) {
        productDto.setId(id); // ensure the correct product is updated
        ProductDto updatedProduct = productService.updateProduct(productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete product
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Product Image endpoints
    @PostMapping("/{productId}/image")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<ProductImage> uploadImage(
            @RequestParam MultipartFile file,
            @PathVariable Long productId) throws IOException {
        ProductImage image = productService.addImage(productId, file);
        return ResponseEntity.ok(image);
    }

    @GetMapping("/{productId}/image/{imageId}")
    public ResponseEntity<ProductImage> getImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        ProductImage image = productService.getImageById(productId, imageId);
        return ResponseEntity.ok(image);
    }

    @GetMapping("/{productId}/image")
    public ResponseEntity<List<ProductImage>> getProductImages(@PathVariable Long productId) {
        List<ProductImage> images = productService.getProductImages(productId);
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/{productId}/image/{imageId}")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<Void> deleteProductImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        productService.deleteProductImage(productId, imageId);
        return ResponseEntity.noContent().build();
    }
}
