package com.example.petify.mapper.notification;

import com.example.petify.model.profile.Notification;
import com.example.petify.dto.notification.NotificationListResponse;
import com.example.petify.dto.notification.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationMapper {
    
    public NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.getIsRead())
                .build();
    }
    
    public List<NotificationResponse> toResponseList(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toResponse)
                .toList();
    }
    
    public NotificationListResponse toPageResponse(Page<Notification> notificationPage) {
        List<NotificationResponse> notifications = toResponseList(notificationPage.getContent());
        
        return NotificationListResponse.builder()
                .notifications(notifications)
                .page(notificationPage.getNumber())
                .size(notificationPage.getSize())
                .totalElements(notificationPage.getTotalElements())
                .totalPages(notificationPage.getTotalPages())
                .hasNext(notificationPage.hasNext())
                .hasPrevious(notificationPage.hasPrevious())
                .build();
    }
}