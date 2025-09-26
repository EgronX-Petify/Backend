package com.example.petify.pet.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RejectAppointmentRequest {
    
    @NotBlank(message = "Rejection reason is required")
    private String rejectionReason;
}