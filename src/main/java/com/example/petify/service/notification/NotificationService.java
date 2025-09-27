package com.example.petify.service.notification;

import com.example.petify.model.profile.Notification;
import com.example.petify.model.profile.NotificationType;
import com.example.petify.model.profile.Profile;
import com.example.petify.model.service.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    
    // Core notification operations
    Notification createNotification(Profile recipient, String title, String message, NotificationType type);
    Page<Notification> getNotifications(Profile recipient, Pageable pageable);
    Page<Notification> getUnreadNotifications(Profile recipient, Pageable pageable);
    long getUnreadCount(Profile recipient);
    long getAllNotificationsCount(Profile profile);
    void markAsRead(Long notificationId, Long recipientId);
    void markAllAsRead(Long recipientId);
    
    // Business logic for specific events
    void sendWelcomeNotification(Profile recipient);
    void sendAppointmentCreatedNotification(Appointment appointment);
    void sendNewAppointmentRequestNotification(Appointment appointment);
    void sendAppointmentApprovedNotification(Appointment appointment);
    void sendAppointmentCompletedNotification(Appointment appointment);
    void sendAppointmentCancelledNotification(Appointment appointment);
    void sendAppointmentRejectedNotification(Appointment appointment);

    
    // System notifications
    void sendSystemMaintenanceNotification(String message);
    void sendProfileUpdateNotification(Profile recipient);
    void sendAppointmentReminderNotification(Appointment appointment);
    
    // Admin action notifications
    void sendServiceProviderApprovedNotification(Profile serviceProviderProfile);
    void sendUserBannedNotification(Profile userProfile);
    void sendUserUnbannedNotification(Profile userProfile);
    
    // Cleanup operations
    void cleanupOldNotifications();


}