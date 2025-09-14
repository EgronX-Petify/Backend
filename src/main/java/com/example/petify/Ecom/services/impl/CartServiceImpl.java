package com.example.petify.Ecom.services.impl;

import com.example.petify.Ecom.dto.CartDto;
import com.example.petify.Ecom.services.CartService;
import com.example.petify.domain.cart.model.Cart;
import com.example.petify.domain.cart.repository.CartRepository;
import com.example.petify.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepo;

    @Override
    public CartDto getCart(Long cartId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found with id " + cartId)
        );
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
