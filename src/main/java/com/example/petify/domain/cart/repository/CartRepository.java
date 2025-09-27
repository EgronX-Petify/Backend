package com.example.petify.domain.cart.repository;

import com.example.petify.domain.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("""
    SELECT c FROM Cart c
    JOIN FETCH c.cartProducts
    JOIN FETCH c.user u
    WHERE c.user.id = :userId
    """)
    Optional<Cart> findByUserId(Long userId);
}
