package com.example.petify.domain.pet.model;

import com.example.petify.domain.profile.model.POProfile;
import com.example.petify.domain.service.model.Appointment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pet")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "profile_id" , nullable = false)
    private POProfile profile;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MedicalRecord> medicalRecords = new HashSet<>();

    private String name;

    @ElementCollection
    @CollectionTable(
            name = "pet_images",
            joinColumns = @JoinColumn(name = "pet_id")
    )
    @Column(name = "image_url")
    @OrderColumn(name = "image_index")
    private List<String> imageUrls = new ArrayList<>();
    private String species;
    private String breed;
    private String gender;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    



    public void addImageUrl(String url){
        this.imageUrls.add(url);
    }

    public void addAppointment(Appointment appointment){
        this.appointments.add(appointment);
    }

    public void addMedicalRecord(MedicalRecord medicalRecord){
        this.medicalRecords.add(medicalRecord);
    }
}
