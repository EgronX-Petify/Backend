package com.example.petify.Ecom.controller;

import com.example.petify.Ecom.dto.CartDto;
import com.example.petify.Ecom.services.CartService;
import com.example.petify.security.UserInfoDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDto> getCart(@AuthenticationPrincipal UserInfoDetails user){
        return ResponseEntity.ok(
                cartService.getCartByUserId(user.getId())
        );
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable long cartId) {
        return ResponseEntity.ok().body(
                cartService.getCart(cartId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addItemToCart(@Valid CartDto.CartProductDto cartProduct) {
        return ResponseEntity.ok().body(
                cartService.addItemToCart(cartProduct)
        );
    }

    @PostMapping("/remove/{productId}")
    public ResponseEntity<CartDto> removeItemFromCart(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                cartService.removeItemFromCart(productId) // get the product id
        );
    }

    @PutMapping("/add")
    public ResponseEntity<CartDto> updateItemQuantity(@Valid CartDto.CartProductDto cartProduct) {
        return ResponseEntity.status(HttpStatus.OK).body(
                cartService.updateItemQuantity(cartProduct)
        );
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(
            @AuthenticationPrincipal UserInfoDetails userDetails
    ) {
        Long userId = userDetails.getId();
        cartService.clearCart(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                "Cart has been cleared"
        );
    }
}
