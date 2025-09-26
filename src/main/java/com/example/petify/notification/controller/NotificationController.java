package com.example.petify.notification.controller;

import com.example.petify.auth.services.AuthenticatedUserService;
import com.example.petify.domain.profile.model.Profile;
import com.example.petify.notification.service.NotificationService;
import com.example.petify.domain.user.model.User;
import com.example.petify.notification.dto.NotificationCountResponse;
import com.example.petify.notification.dto.NotificationListResponse;
import com.example.petify.notification.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/me/notification")
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final AuthenticatedUserService authenticatedUserService;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'PET_OWNER')")
    public ResponseEntity<NotificationListResponse> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean unreadOnly) {
        
        User currentUser = authenticatedUserService.getCurrentUser();
        Profile profile = currentUser.getProfile();
        
        Pageable pageable = PageRequest.of(page, size);
        
        var notificationPage = unreadOnly ? notificationService.getUnreadNotifications(profile, pageable)
            : notificationService.getNotifications(profile, pageable);
        
        NotificationListResponse response = notificationMapper.toPageResponse(notificationPage);
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'PET_OWNER')")
    public ResponseEntity<NotificationCountResponse> getNotificationCount() {
        User currentUser = authenticatedUserService.getCurrentUser();
        Profile profile = currentUser.getProfile();

        long unreadCount = notificationService.getUnreadCount(profile);
        long count = notificationService.getAllNotificationsCount(profile);

        NotificationCountResponse response = NotificationCountResponse.builder()
                .unreadCount(unreadCount)
                .totalCount(count)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{notificationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'PET_OWNER')")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        User currentUser = authenticatedUserService.getCurrentUser();
        Profile profile = currentUser.getProfile();
        
        notificationService.markAsRead(notificationId, profile.getId());
        
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/mark-all-read")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'PET_OWNER')")
    public ResponseEntity<Void> markAllAsRead() {
        User currentUser = authenticatedUserService.getCurrentUser();
        Profile profile = currentUser.getProfile();
        
        notificationService.markAllAsRead(profile.getId());
        
        return ResponseEntity.ok().build();
    }
}