package com.example.petify.dto.user.serviceprovider;

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
public class ApproveAppointmentRequest {
    
    @NotNull(message = "Scheduled time is required")
    private LocalDateTime scheduledTime;
    
    private String notes;
}