package com.example.petify.domain.user.model;


import com.example.petify.domain.profile.model.Profile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "`user`")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    private String location;

    @CreationTimestamp
    @Column(name= "created_at",nullable = false,  updatable = false)
    private LocalDateTime createdAt;




    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
    Profile profile;

}
