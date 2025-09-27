package com.example.petify.service.notification.impl;

import com.example.petify.service.common.EmailService;
import com.example.petify.model.profile.Notification;
import com.example.petify.model.profile.NotificationType;
import com.example.petify.model.profile.Profile;
import com.example.petify.repository.profile.NotificationRepository;
import com.example.petify.service.notification.NotificationService;
import com.example.petify.model.service.Appointment;
import com.example.petify.repository.user.UserRepository;
import com.example.petify.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Override
    public Notification createNotification(Profile recipient, String title, String message, NotificationType type) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .title(title)
                .message(message)
                .type(type)
                .isRead(false)
                .build();

        return notificationRepository.save(notification);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Notification> getNotifications(Profile recipient, Pageable pageable) {
        return notificationRepository.findByRecipientOrderByCreatedAtDesc(recipient, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Notification> getUnreadNotifications(Profile recipient, Pageable pageable) {
        return notificationRepository.findByRecipientAndIsReadFalseOrderByCreatedAtDesc(recipient, pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public long getAllNotificationsCount(Profile recipient) {
        return notificationRepository.countByRecipient(recipient);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(Profile recipient) {
        return notificationRepository.countByRecipientAndIsReadFalse(recipient);
    }


    @Override
    public void markAsRead(Long notificationId, Long recipientId) {
        int updated = notificationRepository.markAsRead(notificationId, recipientId);
        if (updated == 0) {
            throw new ResourceNotFoundException("Notification not found or doesn't belong to user");
        }
    }
    
    @Override
    public void markAllAsRead(Long recipientId) {
        int updated = notificationRepository.markAllAsRead(recipientId);
    }
    
    @Override
    public void sendWelcomeNotification(Profile recipient) {
        String title = "Welcome to Petify! 🐾";
        String message = String.format("Hello %s! Welcome to Petify, your trusted pet care companion. " +
                "We're excited to help you take the best care of your furry friends. " +
                "Explore our services and connect with professional pet care providers in your area.",
                recipient.getName() != null ? recipient.getName() : "Pet Lover");
        
        createNotification(recipient, title, message, NotificationType.WELCOME);
        emailService.sendEmail(fromEmail, recipient.getUser().getEmail(), title, message);
    }
    
    @Override
    public void sendAppointmentCreatedNotification(Appointment appointment) {
        Profile petOwner = appointment.getPet().getProfile();
        String title = "Appointment Request Submitted";
        String message = String.format("Your appointment request for %s with %s has been submitted successfully. " +
                "Requested time: %s. You'll be notified once the service provider responds.",
                appointment.getPet().getName(),
                appointment.getService().getName(),
                appointment.getRequestedTime().toString());
        
        createNotification(petOwner, title, message, NotificationType.APPOINTMENT_CREATED);
        emailService.sendEmail(fromEmail, petOwner.getUser().getEmail(), title, message);
    }
    
    @Override
    public void sendAppointmentApprovedNotification(Appointment appointment) {
        Profile petOwner = appointment.getPet().getProfile();
        String title = "Appointment Approved ✅";
        String message = String.format("Great news! Your appointment for %s has been approved. " +
                "Scheduled time: %s. Please arrive on time and bring your pet ready for the service.",
                appointment.getPet().getName(),
                appointment.getScheduledTime().toString());
        
        createNotification(petOwner, title, message, NotificationType.APPOINTMENT_APPROVED);
        emailService.sendEmail(fromEmail, petOwner.getUser().getEmail(), title, message);
    }
    
    @Override
    public void sendAppointmentCompletedNotification(Appointment appointment) {
        Profile petOwner = appointment.getPet().getProfile();
        String title = "Service Completed 🎉";
        String message = String.format("The %s service for %s has been completed successfully. " +
                "Thank you for choosing our service! We hope you and your pet had a great experience.",
                appointment.getService().getName(),
                appointment.getPet().getName());
        
        createNotification(petOwner, title, message, NotificationType.APPOINTMENT_COMPLETED);
        emailService.sendEmail(fromEmail, petOwner.getUser().getEmail(), title, message);
    }
    
    @Override
    public void sendAppointmentCancelledNotification(Appointment appointment) {
        Profile petOwner = appointment.getPet().getProfile();
        String title = "Appointment Cancelled";
        String POMessage = String.format("Your appointment for %s scheduled for %s has been cancelled. " +
                        "If you have any questions, please contact our support team.",
                appointment.getPet().getName(),
                appointment.getScheduledTime() != null ? appointment.getScheduledTime().toString() : appointment.getRequestedTime().toString());

        Profile serviceProvider = appointment.getService().getProvider();
        String SPMessage = String.format("Your appointment for %s scheduled for %s has been cancelled. " +
                        "If you have any questions, please contact our support team.",
                appointment.getService().getName(),
                appointment.getScheduledTime() != null ? appointment.getScheduledTime().toString() : appointment.getRequestedTime().toString());

        createNotification(petOwner, title, POMessage , NotificationType.APPOINTMENT_CANCELLED);
        createNotification(serviceProvider, title, SPMessage , NotificationType.APPOINTMENT_CANCELLED);
        emailService.sendEmail(fromEmail, petOwner.getUser().getEmail(), title, POMessage);
        emailService.sendEmail(fromEmail, serviceProvider.getUser().getEmail(), title, SPMessage);
    }
    
    @Override
    public void sendAppointmentRejectedNotification(Appointment appointment) {
        Profile petOwner = appointment.getPet().getProfile();
        String title = "Appointment Request Declined";
        String message = String.format("Unfortunately, your appointment request for %s has been declined. " +
                "%s Please feel free to request a different time or try another service provider.",
                appointment.getPet().getName(),
                appointment.getRejectionReason() != null ? "Reason: " + appointment.getRejectionReason() + ". " : "");
        
        createNotification(petOwner, title, message, NotificationType.APPOINTMENT_REJECTED);
        emailService.sendEmail(fromEmail, petOwner.getUser().getEmail(), title, message);
    }
    
    @Override
    public void sendNewAppointmentRequestNotification(Appointment appointment) {
        Profile serviceProvider = appointment.getService().getProvider();
        String title = "New Appointment Request 📅";
        String message = String.format("You have a new appointment request for %s. " +
                "Pet: %s, Requested time: %s. Please review and respond promptly.",
                appointment.getService().getName(),
                appointment.getPet().getName(),
                appointment.getRequestedTime().toString());
        
        createNotification(serviceProvider, title, message, NotificationType.NEW_APPOINTMENT_REQUEST);
        emailService.sendEmail(fromEmail, serviceProvider.getUser().getEmail(), title, message);
    }
    
    @Override
    public void sendSystemMaintenanceNotification(String message) {
        List<Profile> allProfiles = userRepository.findAll().stream()
                .map(user -> user.getProfile())
                .toList();
        
        String title = "System Maintenance Notice";
        for (Profile profile : allProfiles) {
            createNotification(profile, title, message, NotificationType.SYSTEM_MAINTENANCE);
            emailService.sendEmail(fromEmail, profile.getUser().getEmail(), title, message);
        }
    }
    
    @Override
    public void sendProfileUpdateNotification(Profile recipient) {
        String title = "Profile Updated";
        String message = "Your profile information has been updated successfully. " +
                "If you didn't make these changes, please contact our support team immediately.";
        
        createNotification(recipient, title, message, NotificationType.PROFILE_UPDATE);
        emailService.sendEmail(fromEmail, recipient.getUser().getEmail(), title, message);
    }
    
    @Override
    public void sendAppointmentReminderNotification(Appointment appointment) {
        Profile petOwner = appointment.getPet().getProfile();
        String title = "Appointment Reminder 🔔";
        String message = String.format("Don't forget! You have an appointment for %s scheduled for %s. " +
                "Please arrive on time with your pet ready for the service.",
                appointment.getPet().getName(),
                appointment.getScheduledTime().toString());
        
        createNotification(petOwner, title, message, NotificationType.APPOINTMENT_REMINDER);
        emailService.sendEmail(fromEmail, petOwner.getUser().getEmail(), title, message);
    }
    
    @Override
    public void cleanupOldNotifications() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        notificationRepository.deleteOldReadNotifications(cutoffDate);
    }

    @Override
    public void sendServiceProviderApprovedNotification(Profile serviceProviderProfile) {
        String title = "Welcome to Petify! 🎉";
        String message = String.format("Congratulations, %s! Your service provider account has been approved by our admin team. " +
                "You can now start offering your services to pet owners in your area. " +
                "Thank you for joining our community of trusted pet care professionals!",
                serviceProviderProfile.getName() != null ? serviceProviderProfile.getName() : "Service Provider");
        
        createNotification(serviceProviderProfile, title, message, NotificationType.SERVICE_PROVIDER_APPROVED);
        emailService.sendEmail(fromEmail, serviceProviderProfile.getUser().getEmail(), title, message);
    }
    
    @Override
    public void sendUserBannedNotification(Profile userProfile) {
        String title = "Account Suspended";
        String message = String.format("Dear %s, your Petify account has been suspended due to violations of our terms of service. " +
                "If you believe this is a mistake or would like to appeal this decision, please contact our support team. " +
                "We appreciate your understanding.",
                userProfile.getName() != null ? userProfile.getName() : "User");
        
        createNotification(userProfile, title, message, NotificationType.USER_BANNED);
        emailService.sendEmail(fromEmail, userProfile.getUser().getEmail(), title, message);
    }
    
    @Override
    public void sendUserUnbannedNotification(Profile userProfile) {
        String title = "Account Reactivated ✅";
        String message = String.format("Good news, %s! Your Petify account has been reactivated. " +
                "You can now access all features and services again. " +
                "We're glad to have you back in our pet care community!",
                userProfile.getName() != null ? userProfile.getName() : "User");
        
        createNotification(userProfile, title, message, NotificationType.USER_UNBANNED);
        emailService.sendEmail(fromEmail, userProfile.getUser().getEmail(), title, message);
    }

}