package com.example.petify.dto.auth;


import com.example.petify.dto.user.UserProfileResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private UserResponse user;
}
