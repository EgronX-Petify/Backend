package com.example.petify.domain.cart.model;

import com.example.petify.domain.profile.model.POProfile;
import com.example.petify.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "cart")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<CartProduct> cartProducts;

    public void addCartProduct(CartProduct cartProduct){
        cartProduct.setCart(this);
        for (CartProduct existing : cartProducts) {
            if (existing.getProduct().getId().equals(cartProduct.getProduct().getId())) {
                existing.setQuantity(existing.getQuantity() + cartProduct.getQuantity());
                return;
            }
        }
        cartProducts.add(cartProduct);
    }

    public void removeCartProduct(Long productId){
        this.cartProducts.removeIf(
                cartProduct -> cartProduct.getProduct().getId().equals(productId));
    }

}
