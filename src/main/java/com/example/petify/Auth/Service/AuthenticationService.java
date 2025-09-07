package com.example.petify.Auth.Service;

import com.example.petify.Auth.DTO.SignupRequest;
import com.example.petify.Auth.DTO.SignupResponse;
import com.example.petify.security.UserInfoDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;


public interface AuthenticationService {
    UserDetails authenticate(String email, String password);
    SignupResponse RegisterUser(SignupRequest signupRequest);
    String GenerateToken(UserInfoDetails userDetails);
}
