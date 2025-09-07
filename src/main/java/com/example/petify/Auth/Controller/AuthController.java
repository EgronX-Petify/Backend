package com.example.petify.Auth.Controller;


import com.example.petify.Auth.DTO.SignupRequest;
import com.example.petify.Auth.DTO.SignupResponse;
import com.example.petify.Auth.Service.AuthenticationService;
import com.example.petify.Auth.DTO.LoginRequest;
import com.example.petify.Auth.DTO.LoginResponse;
import com.example.petify.security.UserInfoDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

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
}
