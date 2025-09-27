package com.example.petify.dto.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatusCountDto {
    private Long activeUsers;
    private Long pendingUsers;
    private Long bannedUsers;
    private Long totalUsers;
}