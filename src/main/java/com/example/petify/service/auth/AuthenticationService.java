package com.example.petify.service.auth;

import com.example.petify.dto.auth.SignupRequest;
import com.example.petify.dto.auth.SignupResponse;
import com.example.petify.config.security.UserInfoDetails;
import org.springframework.security.core.userdetails.UserDetails;


public interface AuthenticationService {
    UserDetails authenticate(String email, String password);
    String generateAccessToken(UserInfoDetails userDetails);
    SignupResponse registerUser(SignupRequest signupRequest);

}
