package com.example.petify.domain.profile.model;


import com.example.petify.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ElementCollection
    @CollectionTable(
            name = "profile_images",
            joinColumns = @JoinColumn(name = "profile_id")
    )
    @Column(name = "image_url")
    @OrderColumn(name = "image_index")
    private List<String> imageUrls = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;


    @Column(nullable = false)
    private String phoneNumber;

    public void addImageUrl(String imageUrl) {
        this.imageUrls.add(imageUrl);
    }
}
