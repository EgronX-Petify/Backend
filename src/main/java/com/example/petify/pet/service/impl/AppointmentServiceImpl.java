package com.example.petify.pet.service.impl;

import com.example.petify.auth.services.AuthenticatedUserService;
import com.example.petify.domain.pet.model.MedicalRecord;
import com.example.petify.domain.pet.model.Pet;
import com.example.petify.domain.pet.repository.MedicalRecordRepository;
import com.example.petify.domain.pet.repository.PetRepository;
import com.example.petify.notification.service.NotificationService;
import com.example.petify.domain.service.model.Appointment;
import com.example.petify.domain.service.model.AppointmentStatus;
import com.example.petify.domain.service.model.Services;
import com.example.petify.domain.service.repository.AppointmentRepository;
import com.example.petify.domain.service.repository.ServiceRepository;
import com.example.petify.domain.user.model.User;
import com.example.petify.domain.user.repository.UserRepository;
import com.example.petify.exception.ResourceNotFoundException;
import com.example.petify.pet.dto.AppointmentResponse;
import com.example.petify.pet.dto.ApproveAppointmentRequest;
import com.example.petify.pet.dto.CreateAppointmentRequest;
import com.example.petify.pet.dto.RejectAppointmentRequest;
import com.example.petify.pet.mapper.AppointmentMapper;
import com.example.petify.pet.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PetRepository petRepository;
    private final ServiceRepository serviceRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentMapper appointmentMapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final NotificationService notificationService;

    @Override
    public AppointmentResponse createAppointment(CreateAppointmentRequest request) {

        User user = authenticatedUserService.getCurrentUser();
        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + request.getPetId()));
        Services service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + request.getServiceId()));

        if(!pet.getProfile().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not the owner of this pet");
        }

        Appointment appointment = Appointment.builder()
                .pet(pet)
                .service(service)
                .requestedTime(request.getRequestedTime())
                .status(AppointmentStatus.PENDING)
                .notes(request.getNotes())
                .build();

        appointment = appointmentRepository.save(appointment);
        
        //## Send notifications ##//
        // Notify pet owner that appointment was created
        notificationService.sendAppointmentCreatedNotification(appointment);
        // Notify service provider about new appointment request
        notificationService.sendNewAppointmentRequestNotification(appointment);
        
        return appointmentMapper.mapToResponse(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getAppointmentById(Long appointmentId) {

        Appointment appointment = checkAuthority(appointmentId);
        return appointmentMapper.mapToResponse(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByPet(Long petId) {
        User user = authenticatedUserService.getCurrentUser();
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + petId));
        if(!pet.getProfile().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not the owner of this pet");
        }
        List<Appointment> appointments = appointmentRepository.findByPetId(pet.getId());
        return appointments.stream()
                .map(appointmentMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getMyAppointmentsByService(Long serviceId) {
        var user = authenticatedUserService.getCurrentUser();
        var service = serviceRepository.findById(serviceId).orElseThrow(
                () -> new ResourceNotFoundException("Service not found with id: " + serviceId));

        if (!service.getProvider().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not the owner of this service");
        }

        List<Appointment> appointments = appointmentRepository.findByService(service);
        return appointments.stream()
                .map(appointmentMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getAppointmentsByPetOwner() {
        User user = authenticatedUserService.getCurrentUser();
        List<Appointment> appointments = appointmentRepository.findByPetOwnerId(user.getId());
        return appointments.stream()
                .map(appointmentMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getProviderAppointments() {

        var user = authenticatedUserService.getCurrentUser();

        List<Appointment> appointments = appointmentRepository.findByServiceProviderUserId(user.getId());
        return appointments.stream()
                .map(appointmentMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getUpcomingAppointmentsByPet(Long petId) {
        User user = authenticatedUserService.getCurrentUser();
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + petId));
        if(!pet.getProfile().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not the owner of this pet");
        }

        List<Appointment> appointments = appointmentRepository.findUpcomingAppointmentsByPetId(petId, LocalDateTime.now());
        return appointments.stream()
                .map(appointmentMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getPastAppointmentsByPet(Long petId) {
        User user = authenticatedUserService.getCurrentUser();
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + petId));
        if(!pet.getProfile().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not the owner of this pet");
        }
        List<Appointment> appointments = appointmentRepository.findPastAppointmentsByPetId(petId, LocalDateTime.now());
        return appointments.stream()
                .map(appointmentMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getUpcomingAppointmentsByPetOwner() {
        User user = authenticatedUserService.getCurrentUser();
        List<Appointment> appointments = appointmentRepository.findUpcomingAppointmentsByPetOwnerId(user.getId(), LocalDateTime.now());
        return appointments.stream()
                .map(appointmentMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> getPastAppointmentsByPetOwner() {
        User user = authenticatedUserService.getCurrentUser();
        List<Appointment> appointments = appointmentRepository.findPastAppointmentsByPetOwnerId(user.getId(), LocalDateTime.now());
        return appointments.stream()
                .map(appointmentMapper::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public void cancelAppointment(Long appointmentId) {

        Appointment appointment = checkAuthority(appointmentId);
        notificationService.sendAppointmentCancelledNotification(appointment);
        
        appointmentRepository.delete(appointment);
    }


    @Override
    public AppointmentResponse approveAppointment(Long appointmentId, ApproveAppointmentRequest request) {

        Appointment appointment = checkAuthority(appointmentId);

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new IllegalStateException("Only pending appointments can be approved");
        }


        appointment.setStatus(AppointmentStatus.APPROVED);
        appointment.setScheduledTime(request.getScheduledTime());
        if (request.getNotes() != null && !request.getNotes().isEmpty()) {
            appointment.setNotes(request.getNotes());
        }

        appointment = appointmentRepository.save(appointment);
        notificationService.sendAppointmentApprovedNotification(appointment);
        
        return appointmentMapper.mapToResponse(appointment);
    }

    @Override
    public AppointmentResponse rejectAppointment(Long appointmentId, RejectAppointmentRequest request) {
        Appointment appointment = checkAuthority(appointmentId);
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new IllegalStateException("Only pending appointments can be rejected");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setRejectionReason(request.getRejectionReason());

        appointment = appointmentRepository.save(appointment);

        notificationService.sendAppointmentRejectedNotification(appointment);
        
        return appointmentMapper.mapToResponse(appointment);
    }

    @Override
    public AppointmentResponse completeAppointment(Long appointmentId) {
        Appointment appointment = checkAuthority(appointmentId);

        if (appointment.getStatus() != AppointmentStatus.APPROVED) {
            throw new IllegalStateException("Only approved appointments can be completed");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment = appointmentRepository.save(appointment);
        
        createMedicalRecord(appointment);
        notificationService.sendAppointmentCompletedNotification(appointment);

        return appointmentMapper.mapToResponse(appointment);
    }
    
    private void createMedicalRecord(Appointment appointment) {
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .pet(appointment.getPet())
                .visitDate(appointment.getScheduledTime().toLocalDate())
                .reasonForVisit(appointment.getService().getName())
                .note(appointment.getNotes())
                .build();

        medicalRecordRepository.save(medicalRecord);
    }

    private Appointment checkAuthority(Long appointmentId) {
        var user = authenticatedUserService.getCurrentUser();
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        boolean validPO = appointment.getPet().getProfile().getUser().getId().equals(user.getId());
        boolean validSP = appointment.getService().getProvider().getUser().getId().equals(user.getId());
        if (!(validPO || validSP)) {
            throw new IllegalArgumentException("User is not part of this appointment");
        }
        return appointment;
    }

}