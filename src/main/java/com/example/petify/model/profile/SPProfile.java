package com.example.petify.model.profile;

import com.example.petify.model.product.Product;
import com.example.petify.model.service.Services;
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



    @Column
    private String description;

    @Column(name = "contact_info")
    private String contactInfo;

    @OneToMany(mappedBy = "provider" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Services> services = new HashSet<>();

    @OneToMany(mappedBy = "seller" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Product> products = new HashSet<>();


    public void addBranch(Services service){
        this.services.add(service);
    }

    public void addProduct(Product product){
        this.products.add(product);
    }


}
