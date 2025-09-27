package com.example.petify.model.order;

import com.example.petify.model.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_product")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id" , nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id" , nullable = false)
    private Product product;

    private double priceAtOrder;

    @Column(nullable = false)
    @Builder.Default
    private int quantity = 1;
}
