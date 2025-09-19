package com.example.petify.domain.profile.model;


import com.example.petify.domain.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "profile")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;



    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.EAGER)
    private Set<ProfileImage> images = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;


    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> notifications = new HashSet<>();

    @Column
    private String name;

    @Column
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;


    public void addImage(ProfileImage image) {
        this.images.add(image);
    }
    public void addNotification(Notification notification){
        this.notifications.add(notification);
    }
}
