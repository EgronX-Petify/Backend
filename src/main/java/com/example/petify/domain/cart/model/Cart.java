package com.example.petify.domain.cart.model;

import com.example.petify.domain.profile.model.POProfile;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @JoinColumn(name = "profile_id")
    private POProfile profile;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.EAGER)
    private List<CartProduct> cartProducts;



    public void addCartProduct(CartProduct cartProduct){
        this.cartProducts.add(cartProduct);
    }
}
