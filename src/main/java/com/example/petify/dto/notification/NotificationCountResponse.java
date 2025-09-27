package com.example.petify.dto.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationCountResponse {
    private long unreadCount;
    private long totalCount;
}