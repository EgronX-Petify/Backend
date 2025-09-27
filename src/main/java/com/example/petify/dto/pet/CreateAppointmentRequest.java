package com.example.petify.dto.pet;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentRequest {
    @NotNull(message = "Pet ID is required")
    private Long petId;
    
    @NotNull(message = "Service ID is required")
    private Long serviceId;

    private LocalDateTime requestedTime;
    
    private String notes;
}