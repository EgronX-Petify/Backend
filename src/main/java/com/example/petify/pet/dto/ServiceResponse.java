package com.example.petify.pet.dto;

import com.example.petify.domain.service.model.ServiceCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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