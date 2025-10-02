package com.example.petify.service.ecom.impl;

import com.example.petify.dto.ecom.CartDto;
import com.example.petify.mapper.ecom.CartMapper;
import com.example.petify.model.user.User;
import com.example.petify.repository.user.UserRepository;
import com.example.petify.service.ecom.CartService;
import com.example.petify.model.cart.Cart;
import com.example.petify.model.cart.CartProduct;
import com.example.petify.repository.cart.CartRepository;
import com.example.petify.model.product.Product;
import com.example.petify.repository.product.ProductRepository;
import com.example.petify.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final UserRepository userRepo;
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
    @Transactional
    public CartDto getCartByUserId(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id " + userId)
        );

        Cart cart = cartRepo.findByUserId(userId).orElseGet( () ->
                cartRepo.save(
                        Cart.builder()
                                .user(user)
                                .cartProducts(new HashSet<>())
                                .build()
                )
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
        cartRepo.save(cart);
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

        if (quantity < 1) {
            throw new InvalidParameterException("Quantity should be greater than 0");
        }

        CartProduct cartItem = new CartProduct();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        cart.addCartProduct(cartItem);
        cartRepo.save(cart);
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
