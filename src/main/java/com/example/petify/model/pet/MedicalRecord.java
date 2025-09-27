package com.example.petify.domain.pet.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "medical_record")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id" , nullable = false)
    private Pet pet;

    @Column(name = "visit_date")
    private LocalDate visitDate;

    @Column(name = "reason_for_visit", columnDefinition = "TEXT")
    private String reasonForVisit;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String treatment;

    @Column(columnDefinition = "TEXT")
    private String medication;

    @Column(columnDefinition = "TEXT")
    private String note;

//    private String veterinarian;
}
