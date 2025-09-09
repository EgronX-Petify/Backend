package com.example.petify.domain.product.model;

import com.example.petify.domain.profile.model.Review;
import com.example.petify.domain.profile.model.SPProfile;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @ElementCollection
    @CollectionTable(
            name = "product_images",
            joinColumns = @JoinColumn(name = "product_id")
    )
    @Column(name = "image_url")
    @OrderColumn(name = "image_index")
    private List<String> imageUrls = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column
    private double discount;

    private int stock;


    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;



    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();



}
