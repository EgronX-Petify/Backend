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
public class Services {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id" , nullable = false)
    private SPProfile provider;

    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceCategory category;

    @Column(nullable = false)
    private double price;

    @Column
    private String notes;


    @OneToMany(mappedBy = "service" , cascade = CascadeType.ALL , orphanRemoval = true , fetch = FetchType.EAGER)
    private Set<Appointment> appointments = new HashSet<>();


    public void addAppointment(Appointment appointment){
        this.appointments.add(appointment);
    }
}
