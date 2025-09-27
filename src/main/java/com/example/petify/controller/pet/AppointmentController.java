package com.example.petify.controller.pet;

import com.example.petify.dto.pet.AppointmentResponse;
import com.example.petify.dto.pet.CreateAppointmentRequest;
import com.example.petify.service.pet.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/me/appointment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;


    @PostMapping
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request) {
        AppointmentResponse appointment = appointmentService.createAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }


    @GetMapping("/{appointmentId}")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<AppointmentResponse> getAppointmentById(
            @PathVariable Long appointmentId) {
        AppointmentResponse appointment = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<List<AppointmentResponse>> getAppointments(
            @RequestParam(required = false) Long petId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String timeFilter) {
        
        List<AppointmentResponse> appointments;
        

        if ("upcoming".equals(timeFilter)) {
            if (petId != null) {
                appointments = appointmentService.getUpcomingAppointmentsByPet(petId);
            } else {
                appointments = appointmentService.getUpcomingAppointmentsByPetOwner();
            }
        } else if ("past".equals(timeFilter)) {
            if (petId != null) {
                appointments = appointmentService.getPastAppointmentsByPet(petId);
            } else {
                appointments = appointmentService.getPastAppointmentsByPetOwner();
            }
        } else if (petId != null) {
            appointments = appointmentService.getAppointmentsByPet(petId);
        } else {

            appointments = appointmentService.getAppointmentsByPetOwner();
        }

        if (status != null && !status.isEmpty()) {
            appointments = appointments.stream()
                    .filter(appointment -> status.equalsIgnoreCase(appointment.getStatus()))
                    .toList();
        }
        
        return ResponseEntity.ok(appointments);
    }

    
    @DeleteMapping("/{appointmentId}")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<Void> cancelAppointment(
            @PathVariable Long appointmentId) {
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }
}