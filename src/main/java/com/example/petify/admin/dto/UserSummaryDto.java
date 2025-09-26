package com.example.petify.admin.dto;

import com.example.petify.domain.user.model.UserStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserSummaryDto {
    private Long id;
    private String email;
    private String role;
    private UserStatus status;
    private LocalDateTime createdAt;
}