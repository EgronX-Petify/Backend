package com.example.petify.Ecom.services;

import com.example.petify.Ecom.dto.CartDto;

public interface CartService {
    CartDto getCart(Long cartId);

    CartDto getCartByUserId(Long userId);

    CartDto addItemToCart(Long userId, Long itemId,  Integer quantity);

    CartDto removeItemFromCart(Long userId, Long productId);

    CartDto updateItemQuantity(Long userId, Long productId, Integer quantity);

    void clearUsersCart(Long userId);

}
