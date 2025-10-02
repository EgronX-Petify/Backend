package com.example.petify.model.pet;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pet_image")
@EntityListeners(PetImageListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PetImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String contentType;

    @Column(name = "file_path")
    private String filePath;

    @Transient
    private byte[] data;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;
}
