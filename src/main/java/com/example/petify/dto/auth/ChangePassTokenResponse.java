package com.example.petify.dto.auth;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChangePassTokenResponse {
    private String token;
    private LocalDateTime expiryDate;
}
