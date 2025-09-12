package com.example.petify.domain.service.model;

import com.example.petify.domain.pet.model.Pet;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pet_id" , nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "service_id" , nullable = false)
    private Service service;

    private LocalDateTime time;
    private String status;

    @Column
    private String notes;
}
