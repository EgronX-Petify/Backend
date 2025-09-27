package com.example.petify.dto.pet;

import com.example.petify.model.service.ServiceCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateServiceRequest {
    @NotBlank(message = "Service name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Service category is required")
    private ServiceCategory category;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private double price;
    
    private String notes;
}