package com.example.petify.domain.pet.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pet_image")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PetImage {
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
    @JoinColumn(name = "pet_id")
    private Pet pet;
}
