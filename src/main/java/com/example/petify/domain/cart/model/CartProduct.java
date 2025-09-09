package com.example.petify.domain.cart.model;

import com.example.petify.domain.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_product")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id" , nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id" , nullable = false)
    private Product product;

    @Column(nullable = false)
    @Builder.Default
    private int quantity = 1;
}
