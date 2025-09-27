package com.example.petify.dto.admin;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminActionResponse {
    private String message;
    private boolean success;
}