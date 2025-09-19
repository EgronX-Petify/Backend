package com.example.petify.domain.pet.model;

import com.example.petify.domain.profile.model.POProfile;
import com.example.petify.domain.service.model.Appointment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.EAGER)
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.EAGER)
    private Set<MedicalRecord> medicalRecords = new HashSet<>();

    @NotBlank(message = "Pet name is required")
    private String name;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.EAGER)
    private Set<PetImage> images = new HashSet<>();
    
    @NotBlank(message = "Species is required")
    private String species;
    
    @NotBlank(message = "Breed is required")
    private String breed;
    
    @NotBlank(message = "Gender is required")
    private String gender;
    
    @Column(name = "date_of_birth")
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;
    



    public void addImage(PetImage image){
        this.images.add(image);
    }

    public void addAppointment(Appointment appointment){
        this.appointments.add(appointment);
    }

    public void addMedicalRecord(MedicalRecord medicalRecord){
        this.medicalRecords.add(medicalRecord);
    }

}
