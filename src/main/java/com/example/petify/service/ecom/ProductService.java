package com.example.petify.service.ecom;

import com.example.petify.dto.ecom.ProductDto;
import com.example.petify.dto.ecom.ProductFilter;
import com.example.petify.model.product.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDto getProduct(long id);

    Page<ProductDto> getProducts(
            ProductFilter filter, int limit, int offset);

    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(ProductDto productDto);

    void deleteProduct(long id);

    // Product Image methods
    ProductImage addImage(Long productId, MultipartFile file);

    ProductImage getImageById(Long productId, Long imageId);

    List<ProductImage> getProductImages(Long productId);

    void deleteProductImage(Long productId, Long imageId);
}
