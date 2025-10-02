package com.example.petify.controller.user.serviceprovider;

import com.example.petify.dto.pet.*;
import com.example.petify.dto.user.serviceprovider.ApproveAppointmentRequest;
import com.example.petify.dto.user.serviceprovider.CreateServiceRequest;
import com.example.petify.dto.user.serviceprovider.RejectAppointmentRequest;
import com.example.petify.dto.user.serviceprovider.ServiceProviderResponse;
import com.example.petify.service.pet.AppointmentService;
import com.example.petify.service.user.ServiceProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/provider")
@RequiredArgsConstructor
public class ServiceProviderController {

    ServiceProviderService serviceProviderService;
    AppointmentService appointmentService;

    // ========== PUBLIC ENDPOINTS ==========

    @GetMapping("")
    public ResponseEntity<List<ServiceProviderResponse>> getAllServiceProviders() {
        List<ServiceProviderResponse> providers = serviceProviderService.getAllServiceProviders();
        return ResponseEntity.ok(providers);
    }

    @GetMapping("/{providerId}/service")
    public ResponseEntity<List<ServiceResponse>> getServicesByProvider(@PathVariable Long providerId) {
        List<ServiceResponse> services = serviceProviderService.getServicesByProvider(providerId);
        return ResponseEntity.ok(services);
    }

    // ========== SERVICE PROVIDER ENDPOINTS ==========

    @GetMapping("/me/service")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<List<ServiceResponse>> getMyServices() {
        List<ServiceResponse> services = serviceProviderService.getMyServices();
        return ResponseEntity.ok(services);
    }


    @PostMapping("/me/service")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<ServiceResponse> createService(
            @Valid @RequestBody CreateServiceRequest request) {
        ServiceResponse service = serviceProviderService.createService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(service);
    }

    @PutMapping("/me/service/{serviceId}")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<ServiceResponse> updateService(
            @PathVariable Long serviceId,
            @Valid @RequestBody CreateServiceRequest request) {
        ServiceResponse service = serviceProviderService.updateService(serviceId , request);
        return ResponseEntity.ok(service);
    }

    @DeleteMapping("/me/service/{serviceId}")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<Void> deleteService(@PathVariable Long serviceId) {
        serviceProviderService.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }


    // ========== SERVICE PROVIDER APPOINTMENT MANAGEMENT ==========

    @GetMapping("/me/service/{serviceId}/appointment")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByService(
            @PathVariable Long serviceId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String timeFilter) { // "upcoming", "past", or null for all

        List<AppointmentResponse> appointments = appointmentService.getMyAppointmentsByService(serviceId);

        if ("upcoming".equals(timeFilter)) {
            appointments = appointments.stream()
                    .filter(appointment -> appointment.getScheduledTime()!=null && appointment.getScheduledTime().isAfter(java.time.LocalDateTime.now()))
                    .sorted((a, b) -> a.getScheduledTime().compareTo(b.getScheduledTime()))
                    .toList();
        } else if ("past".equals(timeFilter)) {
            appointments = appointments.stream()
                    .filter(appointment -> appointment.getScheduledTime()!=null && appointment.getScheduledTime().isBefore(java.time.LocalDateTime.now()))
                    .sorted((a, b) -> b.getScheduledTime().compareTo(a.getScheduledTime()))
                    .toList();
        }

        if (status != null && !status.isEmpty()) {
            appointments = appointments.stream()
                    .filter(appointment -> status.equalsIgnoreCase(appointment.getStatus()))
                    .toList();
        }

        return ResponseEntity.ok(appointments);
    }


    @GetMapping("/me/appointment")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByProvider(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String timeFilter) { // "upcoming", "past", or null for all

        List<AppointmentResponse> appointments = appointmentService.getProviderAppointments();

        if ("upcoming".equals(timeFilter)) {
            appointments = appointments.stream()
                    .filter(appointment -> appointment.getScheduledTime()!=null && appointment.getScheduledTime().isAfter(java.time.LocalDateTime.now()))
                    .sorted((a, b) -> a.getScheduledTime().compareTo(b.getScheduledTime()))
                    .toList();
        } else if ("past".equals(timeFilter)) {
            appointments = appointments.stream()
                    .filter(appointment -> appointment.getScheduledTime()!=null && appointment.getScheduledTime().isBefore(java.time.LocalDateTime.now()))
                    .sorted((a, b) -> b.getScheduledTime().compareTo(a.getScheduledTime()))
                    .toList();
        }

        if (status != null && !status.isEmpty()) {
            appointments = appointments.stream()
                    .filter(appointment -> status.equalsIgnoreCase(appointment.getStatus()))
                    .toList();
        }

        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/me/appointment/{appointmentId}")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<AppointmentResponse> getAppointmentById(
            @PathVariable Long appointmentId) {
        AppointmentResponse appointment = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(appointment);
    }


    @PatchMapping("/me/appointment/{appointmentId}/approve")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<AppointmentResponse> approveAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody ApproveAppointmentRequest request) {
        AppointmentResponse appointment = appointmentService.approveAppointment(appointmentId, request);
        return ResponseEntity.ok(appointment);
    }

    @PatchMapping("/me/appointment/{appointmentId}/reject")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<AppointmentResponse> rejectAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody RejectAppointmentRequest request) {
        AppointmentResponse appointment = appointmentService.rejectAppointment(appointmentId, request);
        return ResponseEntity.ok(appointment);
    }

    @PatchMapping("/me/appointment/{appointmentId}/complete")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<AppointmentResponse> completeAppointment(
            @PathVariable Long appointmentId) {
        AppointmentResponse appointment = appointmentService.completeAppointment(appointmentId);
        return ResponseEntity.ok(appointment);
    }
}
