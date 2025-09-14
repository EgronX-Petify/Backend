package com.example.petify.Ecom.mapper;

import com.example.petify.Ecom.dto.CartDto;
import com.example.petify.domain.cart.model.Cart;

public class CartMapper {
    public CartDto toDto(Cart cart) {
        CartDto cartDto =  CartDto.builder()
                .userId(cart.getProfile().getId())
                .build();
        cart.getCartProducts().forEach(cartProduct->{
            cartDto.getProducts().add(
                    CartDto.CartProductDto.builder()
                            .productId(cartProduct.getProduct().getId())
                            .quantity(cartProduct.getQuantity())
                            .build()
            );
        });
        return cartDto;
    }
}
