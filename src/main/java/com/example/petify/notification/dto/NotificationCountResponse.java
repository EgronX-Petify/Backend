package com.example.petify.notification.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationCountResponse {
    private long unreadCount;
    private long totalCount;
}