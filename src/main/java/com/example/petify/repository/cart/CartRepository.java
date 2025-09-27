package com.example.petify.repository.cart;

import com.example.petify.model.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {

    @Query("""
    SELECT c FROM Cart c
    JOIN FETCH c.cartProducts
    JOIN FETCH c.profile pop
    JOIN FETCH pop.user u
    WHERE c.profile.user.id = :userId
    """)
    Optional<Cart> findByUserId(Long userId);
}
