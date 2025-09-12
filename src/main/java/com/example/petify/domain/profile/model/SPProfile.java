package com.example.petify.domain.profile.model;

import com.example.petify.domain.product.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sp_profile")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SPProfile extends Profile {

    @Column(name = "business_name")
    private String businessName;

    @Column
    private String description;

    @OneToMany(mappedBy = "provider" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Branch> branches = new HashSet<>();

    @OneToMany(mappedBy = "seller" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Product> products = new HashSet<>();


    public void addBranch(Branch branch){
        this.branches.add(branch);
    }

    public void addProduct(Product product){
        this.products.add(product);
    }


}
