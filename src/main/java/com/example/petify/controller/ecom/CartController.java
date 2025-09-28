package com.example.petify.controller.ecom;

import com.example.petify.dto.ecom.CartDto;
import com.example.petify.dto.ecom.UpdateCartItemQuantityRequest;
import com.example.petify.service.ecom.CartService;
import com.example.petify.config.security.UserInfoDetails;
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
    public ResponseEntity<CartDto> getCart(@PathVariable Long cartId) {
        return ResponseEntity.ok().body(
                cartService.getCart(cartId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addItemToCart(
            @AuthenticationPrincipal UserInfoDetails user,
            @Valid @RequestBody CartDto.CartProductDto cartProduct) {
        return ResponseEntity.ok().body(
                cartService.addItemToCart(user.getId(), cartProduct.getProductId(), cartProduct.getQuantity())
        );
    }

    @PostMapping("/remove/{productId}")
    public ResponseEntity<CartDto> removeItemFromCart(
            @AuthenticationPrincipal UserInfoDetails user,
            @PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                cartService.removeItemFromCart(user.getId(),productId)
        );
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartDto> updateItemQuantity(
            @AuthenticationPrincipal UserInfoDetails user,
            @PathVariable Long productId,
            @RequestBody UpdateCartItemQuantityRequest quantityRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(
                cartService.updateItemQuantity(user.getId(), productId, quantityRequest.getQuantity())
        );
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(
            @AuthenticationPrincipal UserInfoDetails userDetails
    ) {
        Long userId = userDetails.getId();
        cartService.clearUsersCart(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                "Cart has been cleared"
        );
    }
}
