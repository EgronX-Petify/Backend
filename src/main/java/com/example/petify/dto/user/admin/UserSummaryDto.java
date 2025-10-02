package com.example.petify.dto.admin;

import com.example.petify.model.user.UserStatus;
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