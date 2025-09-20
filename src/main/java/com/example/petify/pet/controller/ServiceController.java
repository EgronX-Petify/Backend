package com.example.petify.pet.controller;

import com.example.petify.domain.service.model.ServiceCategory;
import com.example.petify.pet.dto.AppointmentResponse;
import com.example.petify.pet.dto.ApproveAppointmentRequest;
import com.example.petify.pet.dto.CreateServiceRequest;
import com.example.petify.pet.dto.RejectAppointmentRequest;
import com.example.petify.pet.dto.ServiceResponse;
import com.example.petify.pet.service.AppointmentService;
import com.example.petify.pet.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ServiceController {

    private final ServiceService serviceService;
    private final AppointmentService appointmentService;


    @GetMapping("/service/search")
    public ResponseEntity<List<ServiceResponse>> searchServices(
            @RequestParam String searchTerm) {
        List<ServiceResponse> services = serviceService.searchServices(searchTerm);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/service/categories")
    public ResponseEntity<ServiceCategory[]> getServiceCategories() {
        return ResponseEntity.ok(ServiceCategory.values());
    }


    @GetMapping("/service")
    public ResponseEntity<List<ServiceResponse>> getAllServices(
            @RequestParam(required = false) String category ,
            @RequestParam(required = false) Long providerId) {

        List<ServiceResponse> services = null;

        if((category != null && !category.isEmpty()) && providerId != null){
            services = serviceService.getServicesByProviderAndCategory(providerId, category);
        }
        else if(category != null && !category.isEmpty()){
            services = serviceService.getServicesByCategory(category);
        }
        else if(providerId != null){
            services = serviceService.getServicesByProvider(providerId);
        }
        else{
            services = serviceService.getAllServices();
        }

        return ResponseEntity.ok(services);
    }


    @GetMapping("/service/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        ServiceResponse service = serviceService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    @GetMapping("/provider/me/service")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<List<ServiceResponse>> getMyServices() {
        List<ServiceResponse> services = serviceService.getMyServices();
        return ResponseEntity.ok(services);
    }


    @PostMapping("/provider/me/service")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<ServiceResponse> createService(
            @Valid @RequestBody CreateServiceRequest request) {
        ServiceResponse service = serviceService.createService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(service);
    }

    @PutMapping("/provider/me/service/{serviceId}")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<ServiceResponse> updateService(
            @PathVariable Long serviceId,
            @Valid @RequestBody CreateServiceRequest request) {
        ServiceResponse service = serviceService.updateService(serviceId , request);
        return ResponseEntity.ok(service);
    }

    @DeleteMapping("/provider/me/service/{serviceId}")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<Void> deleteService(@PathVariable Long serviceId) {
        serviceService.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }


    // ========== SERVICE PROVIDER APPOINTMENT MANAGEMENT ==========

    @GetMapping("/provider/me/service/{serviceId}/appointment")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByService(
            @PathVariable Long serviceId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String timeFilter) { // "upcoming", "past", or null for all
        
        List<AppointmentResponse> appointments = appointmentService.getMyAppointmentsByService(serviceId);
        
        if ("upcoming".equals(timeFilter)) {
            appointments = appointments.stream()
                    .filter(appointment -> appointment.getScheduledTime().isAfter(java.time.LocalDateTime.now()))
                    .sorted((a, b) -> a.getScheduledTime().compareTo(b.getScheduledTime()))
                    .toList();
        } else if ("past".equals(timeFilter)) {
            appointments = appointments.stream()
                    .filter(appointment -> appointment.getScheduledTime().isBefore(java.time.LocalDateTime.now()))
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


    @GetMapping("/provider/me/appointment")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByProvider(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String timeFilter) { // "upcoming", "past", or null for all
        
        List<AppointmentResponse> appointments = appointmentService.getProviderAppointments();
        
        if ("upcoming".equals(timeFilter)) {
            appointments = appointments.stream()
                    .filter(appointment -> appointment.getScheduledTime().isAfter(java.time.LocalDateTime.now()))
                    .sorted((a, b) -> a.getScheduledTime().compareTo(b.getScheduledTime()))
                    .toList();
        } else if ("past".equals(timeFilter)) {
            appointments = appointments.stream()
                    .filter(appointment -> appointment.getScheduledTime().isBefore(java.time.LocalDateTime.now()))
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

    @GetMapping("/provider/me/appointment/{appointmentId}")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<AppointmentResponse> getAppointmentById(
            @PathVariable Long appointmentId) {
        AppointmentResponse appointment = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(appointment);
    }


    @PatchMapping("/provider/me/appointment/{appointmentId}/approve")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<AppointmentResponse> approveAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody ApproveAppointmentRequest request) {
        AppointmentResponse appointment = appointmentService.approveAppointment(appointmentId, request);
        return ResponseEntity.ok(appointment);
    }

    @PatchMapping("/provider/me/appointments/{appointmentId}/reject")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<AppointmentResponse> rejectAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody RejectAppointmentRequest request) {
        AppointmentResponse appointment = appointmentService.rejectAppointment(appointmentId, request);
        return ResponseEntity.ok(appointment);
    }

    @PatchMapping("/provider/me/appointments/{appointmentId}/complete")
    @PreAuthorize("hasRole('SERVICE_PROVIDER')")
    public ResponseEntity<AppointmentResponse> completeAppointment(
            @PathVariable Long appointmentId) {
        AppointmentResponse appointment = appointmentService.completeAppointment(appointmentId);
        return ResponseEntity.ok(appointment);
    }

}