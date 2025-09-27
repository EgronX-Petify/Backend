package com.example.petify.auth.dto;


import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String role;
}
