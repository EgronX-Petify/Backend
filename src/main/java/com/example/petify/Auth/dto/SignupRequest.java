package com.example.petify.Auth.dto;


import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String role;
}
