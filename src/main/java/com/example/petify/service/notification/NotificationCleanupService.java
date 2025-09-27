package com.example.petify.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class NotificationCleanupService {
    
    private final NotificationService notificationService;
    
    // Run cleanup every day at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupOldNotifications() {
        notificationService.cleanupOldNotifications();
    }
}