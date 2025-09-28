package com.example.petify.mapper.ecom;

import com.example.petify.dto.ecom.CartDto;
import com.example.petify.model.cart.Cart;

public class CartMapper {
    public static CartDto toDto(Cart cart) {
        CartDto cartDto =  CartDto.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
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
