package com.example.petify.domain.profile.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin_profile")
@Builder
@NoArgsConstructor
@Getter
@Setter
public class AdminProfile extends Profile {

}