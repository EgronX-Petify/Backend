package com.example.petify.notification.service;

import com.example.petify.domain.service.model.Appointment;
import com.example.petify.domain.service.model.AppointmentStatus;
import com.example.petify.domain.service.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentReminderService {
    
    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;
    
    // Run every hour to check for appointments that need reminders
    @Scheduled(fixedRate = 3600000) // 1 hour in milliseconds
    public void sendAppointmentReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusHours(24);

        // Find appointments that are scheduled for tomorrow and haven't been reminded yet
        List<Appointment> upcomingAppointments = appointmentRepository
                .findByStatusAndScheduledTimeBetween(
                    AppointmentStatus.APPROVED,
                    now.plusHours(23),
                    reminderTime
                );
        
        for (Appointment appointment : upcomingAppointments) {
            notificationService.sendAppointmentReminderNotification(appointment);
        }
    }
}