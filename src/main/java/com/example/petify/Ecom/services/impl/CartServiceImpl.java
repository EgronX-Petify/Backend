package com.example.petify.Ecom.services.impl;

import com.example.petify.Ecom.dto.CartDto;
import com.example.petify.Ecom.services.CartService;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    @Override
    public CartDto getCart(Long cartId) {
        return null;
    }

    @Override
    public CartDto getCartByUserId(Long userId) {
        return null;
    }

    @Override
    public CartDto addItemToCart(CartDto.CartProductDto cartProduct) {
        return null;
    }

    @Override
    public CartDto removeItemFromCart(Long productId) {
        return null;
    }

    @Override
    public CartDto updateItemQuantity(CartDto.CartProductDto cartProduct) {
        return null;
    }

    @Override
    public void clearCart(Long userId) {

    }
}
