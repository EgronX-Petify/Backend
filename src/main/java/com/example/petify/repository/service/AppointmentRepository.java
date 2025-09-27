package com.example.petify.repository.service;

import com.example.petify.model.service.Appointment;
import com.example.petify.model.service.AppointmentStatus;
import com.example.petify.model.service.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPetId(Long petId);

    List<Appointment> findByService(Services service);


    @Query("SELECT a FROM Appointment a WHERE a.pet.profile.user.id = :ownerId")
    List<Appointment> findByPetOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT a FROM Appointment a WHERE a.service.provider.user.id = :providerId")
    List<Appointment> findByServiceProviderUserId(@Param("providerId") Long providerId);


    @Query("SELECT a FROM Appointment a WHERE a.pet.id = :petId AND a.scheduledTime > :currentTime AND a.status = 'APPROVED' ORDER BY a.scheduledTime ASC")
    List<Appointment> findUpcomingAppointmentsByPetId(@Param("petId") Long petId, 
                                                      @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT a FROM Appointment a WHERE a.pet.id = :petId AND a.scheduledTime < :currentTime ORDER BY a.scheduledTime DESC")
    List<Appointment> findPastAppointmentsByPetId(@Param("petId") Long petId, 
                                                  @Param("currentTime") LocalDateTime currentTime);


    @Query("SELECT a FROM Appointment a WHERE a.pet.profile.user.id = :ownerId AND a.scheduledTime > :currentTime ORDER BY a.scheduledTime ASC")
    List<Appointment> findUpcomingAppointmentsByPetOwnerId(@Param("ownerId") Long ownerId, 
                                                           @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT a FROM Appointment a WHERE a.pet.profile.user.id = :ownerId AND a.scheduledTime < :currentTime ORDER BY a.scheduledTime DESC")
    List<Appointment> findPastAppointmentsByPetOwnerId(@Param("ownerId") Long ownerId, 
                                                       @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT a FROM Appointment a WHERE a.service.provider.user.id = :providerId AND a.status = 'PENDING' ORDER BY a.createdAt")
    List<Appointment> findPendingAppointmentsByProviderUserId(@Param("providerId") Long providerId);


    @Query("SELECT a FROM Appointment a WHERE a.pet.id = :petId AND a.status = :status ORDER BY a.createdAt DESC")
    List<Appointment> findAppointmentsByPetIdAndStatus(@Param("petId") Long petId, @Param("status") AppointmentStatus status);
    
    List<Appointment> findByStatusAndScheduledTimeBetween(AppointmentStatus status, 
                                                         LocalDateTime startTime, 
                                                         LocalDateTime endTime);
}