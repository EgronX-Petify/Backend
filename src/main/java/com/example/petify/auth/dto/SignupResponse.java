package com.example.petify.auth.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupResponse {
    private String message;
}
