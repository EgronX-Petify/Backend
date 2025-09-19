package com.example.petify.domain.profile.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profile_image")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProfileImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String contentType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
