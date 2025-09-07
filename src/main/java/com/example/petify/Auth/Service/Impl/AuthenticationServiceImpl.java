package com.example.petify.Auth.Service.Impl;


import com.example.petify.Auth.DTO.SignupRequest;
import com.example.petify.Auth.DTO.SignupResponse;
import com.example.petify.Auth.Service.AuthenticationService;
import com.example.petify.domain.User.User;
import com.example.petify.domain.User.UserRepository;
import com.example.petify.exception.UsernameAlreadyExists;
import com.example.petify.security.JwtService;
import com.example.petify.security.UserInfoDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        return userDetailsService.loadUserByUsername(email);
    }

    @Override
    public SignupResponse RegisterUser(SignupRequest signupRequest) {
        if (userRepo.existsByEmail(signupRequest.getEmail())) {
            throw new UsernameAlreadyExists("Email Already Exists");
        }

        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .role(User.Role.getRole(signupRequest.getRole()))
                .build();

        userRepo.save(user);
        return SignupResponse.builder()
                .message("User Successfully Registered")
                .build();
    }

    @Override
    public String GenerateToken(UserInfoDetails userDetails) {
        return jwtService.generateToken(userDetails);
    }
}
