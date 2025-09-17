package com.example.petify.domain.service.model;

import com.example.petify.domain.profile.model.SPProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "service")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id" , nullable = false)
    private SPProfile provider;

    private String name;

    @Column
    private String description;

    @Column
    private double price;

    @Column
    private String notes;

    @Column(name = "available_at")
    private LocalDateTime availableAt;


    @OneToMany(mappedBy = "service" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Appointment> appointments = new HashSet<>();


    public void addAppointment(Appointment appointment){
        this.appointments.add(appointment);
    }
}
