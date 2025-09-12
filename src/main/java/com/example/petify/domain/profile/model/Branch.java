package com.example.petify.domain.profile.model;


import com.example.petify.domain.service.model.Service;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "branch")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    @Column(name = "contact_info")
    private String contactInfo;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    SPProfile provider;

    @OneToMany(mappedBy = "branch" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Service> services = new HashSet<>();



    public void addService(Service service){
        this.services.add(service);
    }
}
