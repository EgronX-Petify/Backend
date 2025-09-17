package com.example.petify.domain.profile.model;

import com.example.petify.domain.product.model.Product;
import com.example.petify.domain.service.model.Service;
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
    private Set<Service> services = new HashSet<>();

    @OneToMany(mappedBy = "seller" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Product> products = new HashSet<>();


    public void addBranch(Service service){
        this.services.add(service);
    }

    public void addProduct(Product product){
        this.products.add(product);
    }


}
