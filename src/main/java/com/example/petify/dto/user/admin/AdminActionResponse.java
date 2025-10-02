package com.example.petify.dto.user.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminActionResponse {
    private String message;
    private boolean success;
}