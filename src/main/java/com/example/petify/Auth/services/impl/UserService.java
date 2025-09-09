package com.example.petify.Auth.services.impl;


import com.example.petify.Auth.dto.ChangePassTokenResponse;
import com.example.petify.Auth.dto.SignupRequest;
import com.example.petify.Auth.dto.SignupResponse;
import com.example.petify.Auth.services.AuthenticationService;
import com.example.petify.Auth.services.passwordResetService;
import com.example.petify.common.services.EmailService;
import com.example.petify.domain.PasswordReset.PasswordResetToken;
import com.example.petify.domain.PasswordReset.PasswordResetTokenRepository;
import com.example.petify.domain.User.User;
import com.example.petify.domain.User.UserRepository;
import com.example.petify.exception.InvalidTokenException;
import com.example.petify.exception.ResourceNotFoundException;
import com.example.petify.exception.UsernameAlreadyExistsException;
import com.example.petify.security.JwtService;
import com.example.petify.security.UserInfoDetails;
import jakarta.persistence.criteria.From;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements AuthenticationService, passwordResetService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepo;
    private final PasswordResetTokenRepository passResetRepo;
    private final EmailService emailService;

    @Value("${spring.mail.username}")
    String FromMail;

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
            throw new UsernameAlreadyExistsException("Email Already Exists");
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

    @Override
    public ChangePassTokenResponse ResetPassword(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with this" + email + "email Not Found"));
        PasswordResetToken passReset = passResetRepo.save(
                PasswordResetToken.builder()
                        .user(user)
                        .active(true)
                        .expiryDate(LocalDateTime.now().plusHours(1))
                        .createdDate(LocalDateTime.now())
                        .build());

        // email the user and give him the token
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String emailBody = String.format(
"""
Hello,
Looks like you have requested a change password token
here it is: %s
Please don't share it with anyone this token is available until %s
""", passReset.getToken(), passReset.getExpiryDate().format(formatter));

        emailService.sendEmail(
                FromMail,
                user.getEmail(),
                "Password Reset Token",
                emailBody
                );

        return ChangePassTokenResponse.builder()
                .token(passReset.getToken().toString())
                .expiryDate(passReset.getExpiryDate())
                .build();
    }

    @Override
    public void ConfirmReset(String token, String password) {
        PasswordResetToken passReset = passResetRepo.findByToken(UUID.fromString(token)).orElseThrow(
                () -> new ResourceNotFoundException("Password Reset Token Not Found")
        );

        if (LocalDateTime.now().isBefore(passReset.getExpiryDate()) && passReset.getActive()) {
            User user = passReset.getUser();
            user.setPassword(passwordEncoder.encode(password));
            userRepo.save(user);
            passReset.setActive(false);
            passResetRepo.save(passReset);
        } else {
            passReset.setActive(false);
            passResetRepo.save(passReset);
            throw new InvalidTokenException("Password Reset Token Expired");
        }
    }
}
