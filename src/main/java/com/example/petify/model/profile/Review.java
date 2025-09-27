package com.example.petify.model.profile;

import com.example.petify.model.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "review" , uniqueConstraints = @UniqueConstraint(columnNames = {"profile", "product"}))
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private POProfile profile;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer rate;

    @Column(columnDefinition = "TEXT")
    private String comment;
}
