package com.example.petify.domain.product.repository;

import com.example.petify.domain.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
