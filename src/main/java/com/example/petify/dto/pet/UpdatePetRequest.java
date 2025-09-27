package com.example.petify.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePetRequest {
    
    private String name;
    private String species;
    private String breed;
    private String gender;
    private LocalDate dateOfBirth;
}