package com.example.petify.service.pet;

import com.example.petify.dto.pet.AppointmentResponse;
import com.example.petify.dto.user.serviceprovider.ApproveAppointmentRequest;
import com.example.petify.dto.pet.CreateAppointmentRequest;
import com.example.petify.dto.user.serviceprovider.RejectAppointmentRequest;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(CreateAppointmentRequest request);

    AppointmentResponse getAppointmentById(Long appointmentId);

    List<AppointmentResponse> getAppointmentsByPet(Long petId);

    List<AppointmentResponse> getMyAppointmentsByService(Long serviceId);

    List<AppointmentResponse> getAppointmentsByPetOwner();

    List<AppointmentResponse> getProviderAppointments();

    List<AppointmentResponse> getUpcomingAppointmentsByPet(Long petId);

    List<AppointmentResponse> getPastAppointmentsByPet(Long petId);

    List<AppointmentResponse> getUpcomingAppointmentsByPetOwner();

    List<AppointmentResponse> getPastAppointmentsByPetOwner();

    void cancelAppointment(Long id);

    AppointmentResponse approveAppointment(Long appointmentId, ApproveAppointmentRequest request);
    
    AppointmentResponse rejectAppointment(Long appointmentId, RejectAppointmentRequest request);
    
    AppointmentResponse completeAppointment(Long appointmentId);

}