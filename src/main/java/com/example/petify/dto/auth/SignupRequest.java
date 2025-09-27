package com.example.petify.dto.auth;


import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String role;
}
