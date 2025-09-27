package com.example.petify.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private Long petId;
    private String petName;
    private Long serviceId;
    private String serviceName;
    private String serviceCategory;
    private LocalDateTime requestedTime;
    private LocalDateTime scheduledTime;
    private String status;
    private String notes;
    private String rejectionReason;
    private String providerName;
    private Long providerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}