package com.example.petify.Auth.services;

import com.example.petify.Auth.dto.SignupRequest;
import com.example.petify.Auth.dto.SignupResponse;
import com.example.petify.security.UserInfoDetails;
import org.springframework.security.core.userdetails.UserDetails;


public interface AuthenticationService {
    UserDetails authenticate(String email, String password);
    String generateAccessToken(UserInfoDetails userDetails);
    SignupResponse registerUser(SignupRequest signupRequest);
}
