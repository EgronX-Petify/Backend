package com.example.petify.service.ecom;

import com.example.petify.dto.ecom.CartDto;

public interface CartService {
    CartDto getCart(Long cartId);

    CartDto getCartByUserId(Long userId);

    CartDto addItemToCart(Long userId, Long itemId,  Integer quantity);

    CartDto removeItemFromCart(Long userId, Long productId);

    CartDto updateItemQuantity(Long userId, Long productId, Integer quantity);

    void clearUsersCart(Long userId);

}
