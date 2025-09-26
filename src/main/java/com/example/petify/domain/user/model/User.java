package com.example.petify.domain.user.model;


import com.example.petify.domain.profile.model.Profile;
import com.example.petify.validation.ValidRoleProfile;
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
@ValidRoleProfile
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false , updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @CreationTimestamp
    @Column(name= "created_at",nullable = false,  updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
    Profile profile;

}
