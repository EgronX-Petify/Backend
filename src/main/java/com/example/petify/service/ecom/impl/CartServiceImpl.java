package com.example.petify.Ecom.services.impl;

import com.example.petify.Ecom.dto.CartDto;
import com.example.petify.Ecom.mapper.CartMapper;
import com.example.petify.Ecom.services.CartService;
import com.example.petify.domain.cart.model.Cart;
import com.example.petify.domain.cart.model.CartProduct;
import com.example.petify.domain.cart.repository.CartRepository;
import com.example.petify.domain.product.model.Product;
import com.example.petify.domain.product.repository.ProductRepository;
import com.example.petify.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    @Override
    public CartDto getCart(Long cartId) {
        Cart cart = cartRepo.findById(cartId).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found with id " + cartId)
        );
        return CartMapper.toDto(cart);
    }

    @Override
    public CartDto getCartByUserId(Long userId) {
        Cart cart = cartRepo.findByUserId(userId).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found with user id " + userId)
        );
        return CartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDto addItemToCart(Long userId, Long itemId, Integer quantity) {
        return EditCartItem(userId, itemId, quantity);
    }

    @Override
    public CartDto removeItemFromCart(Long userId, Long productId) {
        Cart cart = cartRepo.findByUserId(userId).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found with user id " + userId)
        );
        cart.removeCartProduct(productId);
        return CartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDto updateItemQuantity(Long userId, Long itemId, Integer quantity) {
        return EditCartItem(userId, itemId, quantity);
    }

    @Transactional
    protected CartDto EditCartItem(Long userId, Long itemId, Integer quantity) {
        Cart cart = cartRepo.findByUserId(userId).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found with user id " + userId)
        );
        Product product = productRepo.findById(itemId).orElseThrow(
                () -> new ResourceNotFoundException("Product not found with id " + itemId)
        );
        CartProduct cartItem = new CartProduct();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        cart.addCartProduct(cartItem);
        return CartMapper.toDto(cart);
    }

    @Override
    public void clearUsersCart(Long userId) {
        Cart cart = cartRepo.findByUserId(userId).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found with user id " + userId)
        );
        cart.setCartProducts(new HashSet<>());
        cartRepo.save(cart);
    }
}
