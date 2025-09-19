package com.example.petify.domain.product.model;
import com.example.petify.domain.profile.model.Review;
import com.example.petify.domain.profile.model.SPProfile;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private SPProfile seller;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private double price;


    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL , orphanRemoval = true , fetch = FetchType.EAGER)
    private Set<ProductImage> images = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column
    private double discount;

    private int stock;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;



    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL , orphanRemoval = true , fetch = FetchType.EAGER)
    private Set<Review> reviews = new HashSet<>();



}
