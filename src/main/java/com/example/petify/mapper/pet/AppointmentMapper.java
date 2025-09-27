package com.example.petify.mapper.pet;

import com.example.petify.model.service.Appointment;
import com.example.petify.dto.pet.AppointmentResponse;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponse mapToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .petId(appointment.getPet().getId())
                .petName(appointment.getPet().getName())
                .serviceId(appointment.getService().getId())
                .serviceName(appointment.getService().getName())
                .serviceCategory(appointment.getService().getCategory().name())
                .requestedTime(appointment.getRequestedTime())
                .scheduledTime(appointment.getScheduledTime())
                .status(appointment.getStatus().name())
                .notes(appointment.getNotes())
                .rejectionReason(appointment.getRejectionReason())
                .providerName(appointment.getService().getProvider().getName())
                .providerId(appointment.getService().getProvider().getId())
                .createdAt(appointment.getCreatedAt())
                .updatedAt(appointment.getUpdatedAt())
                .build();
    }
}