package com.example.petify.Auth.controller;


import com.example.petify.Auth.dto.*;
import com.example.petify.Auth.services.AuthenticationService;
import com.example.petify.Auth.services.passwordResetService;
import com.example.petify.security.UserInfoDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final passwordResetService passwordResetService;

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserInfoDetails userDetails = (UserInfoDetails) authenticationService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword());

        String token = authenticationService.GenerateToken(userDetails);
        LoginResponse loginResponse = LoginResponse.builder()
                .token(token)
                .build();
        
        return ResponseEntity.status(HttpStatus.OK).body(
            loginResponse
        );
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        SignupResponse signupResponse = authenticationService.RegisterUser(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                signupResponse
        );
    }

    @PostMapping(value = "/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ChangePassRequest req) {
        ChangePassTokenResponse resp = passwordResetService.ResetPassword(req.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(
                resp // TODO: for testing only remove
        );
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ConfirmChangePassRequest req) {
        passwordResetService.ConfirmReset(req.getToken(), req.getNewPassword());
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Password has been changed successfully");
    }
}
