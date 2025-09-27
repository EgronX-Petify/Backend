package com.example.petify.model.profile;

import com.example.petify.model.cart.Cart;
import com.example.petify.model.order.Order;
import com.example.petify.model.pet.Pet;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "po_profile")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class POProfile extends Profile {

    @OneToMany(mappedBy = "profile" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Pet> pets = new HashSet<>();

    @OneToMany(mappedBy = "profile" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();


    @OneToOne(mappedBy = "profile" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "profile" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Order> orders = new HashSet<>();

    public void addPet(Pet pet){
        this.pets.add(pet);
    }

    public void addReview(Review review){
        this.reviews.add(review);
    }

    public void addOrder(Order order){
        this.orders.add(order);
    }

}
