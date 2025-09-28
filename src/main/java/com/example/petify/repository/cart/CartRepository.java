package com.example.petify.repository.cart;

import com.example.petify.model.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("""
    SELECT DISTINCT c FROM Cart c
    LEFT JOIN FETCH c.cartProducts
    WHERE c.user.id = :userId
    """)
    Optional<Cart> findByUserId(Long userId);
}
