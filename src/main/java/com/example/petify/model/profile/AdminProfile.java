package com.example.petify.model.profile;

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