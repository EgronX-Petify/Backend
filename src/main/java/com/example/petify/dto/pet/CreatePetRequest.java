package com.example.petify.dto.pet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePetRequest {
    
    @NotBlank(message = "Pet name is required")
    private String name;
    
    @NotBlank(message = "Species is required")
    private String species;
    
    @NotBlank(message = "Breed is required")
    private String breed;
    
    @NotBlank(message = "Gender is required")
    private String gender;
    
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;
}