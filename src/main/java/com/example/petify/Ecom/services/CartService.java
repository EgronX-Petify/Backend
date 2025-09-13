package com.example.petify.Ecom.services;

import com.example.petify.Ecom.dto.CartDto;

public interface CartService {
    CartDto getCart(Long cartId);

    CartDto getCartByUserId(Long userId);

    CartDto addItemToCart(CartDto.CartProductDto cartProduct);

    CartDto removeItemFromCart(Long productId);

    CartDto updateItemQuantity(CartDto.CartProductDto cartProduct);

    void clearCart(Long userId);

}
