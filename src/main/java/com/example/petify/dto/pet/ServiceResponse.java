package com.example.petify.dto.pet;

import com.example.petify.model.service.ServiceCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {
    private Long id;
    private String name;
    private String description;
    private ServiceCategory category;
    private double price;
    private String notes;
    private String providerName;
    private Long providerId;
}