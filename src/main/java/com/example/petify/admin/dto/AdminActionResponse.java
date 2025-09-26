package com.example.petify.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminActionResponse {
    private String message;
    private boolean success;
}