package com.example.petify.dto.auth;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupResponse {
    private String message;
    private boolean isPending;
}
