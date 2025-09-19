package com.example.petify.auth.services.impl;


import com.example.petify.auth.dto.ChangePassTokenResponse;
import com.example.petify.auth.dto.SignupRequest;
import com.example.petify.auth.dto.SignupResponse;
import com.example.petify.auth.services.AuthenticationService;
import com.example.petify.auth.services.RefreshTokenService;
import com.example.petify.auth.services.passwordResetService;
import com.example.petify.common.services.EmailService;
import com.example.petify.common.utils.SecureTokenGen;
import com.example.petify.domain.profile.model.AdminProfile;
import com.example.petify.domain.profile.model.POProfile;
import com.example.petify.domain.profile.model.Profile;
import com.example.petify.domain.profile.model.SPProfile;
import com.example.petify.notification.service.NotificationService;
import com.example.petify.domain.user.model.PasswordResetToken;
import com.example.petify.domain.user.model.RefreshToken;
import com.example.petify.domain.user.repository.PasswordResetTokenRepository;
import com.example.petify.domain.user.model.Role;
import com.example.petify.domain.user.model.User;
import com.example.petify.domain.user.repository.RefreshTokenRepository;
import com.example.petify.domain.user.repository.UserRepository;
import com.example.petify.exception.InvalidTokenException;
import com.example.petify.exception.ResourceNotFoundException;
import com.example.petify.exception.UsernameAlreadyExistsException;
import com.example.petify.security.JwtService;
import com.example.petify.security.UserInfoDetails;
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
public class UserService
        implements AuthenticationService, passwordResetService, RefreshTokenService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepo;
    private final PasswordResetTokenRepository passResetRepo;
    private final RefreshTokenRepository refreshTokenRepo;
    private final EmailService emailService;
    private final NotificationService notificationService;

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
    public SignupResponse registerUser(SignupRequest signupRequest) {
        if (userRepo.existsByEmail(signupRequest.getEmail())) {
            throw new UsernameAlreadyExistsException("Email Already Exists");
        }
        Profile profile= null;
        if(Role.getRole(signupRequest.getRole()) == Role.SERVICE_PROVIDER){
            profile = new SPProfile();
        }
        else if (Role.getRole(signupRequest.getRole()) == Role.PET_OWNER){
            profile = new POProfile();
        }
        else if (Role.getRole(signupRequest.getRole()) == Role.ADMIN){
            profile = new AdminProfile();
        }

        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .role(Role.getRole(signupRequest.getRole()))
                .profile(profile)
                .build();
        profile.setUser(user);
        userRepo.save(user);
        
        // Send welcome notification
        notificationService.sendWelcomeNotification(profile);


        return SignupResponse.builder()
                .message("User Successfully Registered")
                .build();
    }

    @Override
    public String generateAccessToken(UserInfoDetails userDetails) {
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


    @Override
    public String generateRefreshToken(Long userId) {
        User user =  userRepo.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User with this" + userId + "id Not Found")
        );

        RefreshToken token =  RefreshToken.builder()
                .user(user)
                .token(SecureTokenGen.generate())
                .expiresAt(LocalDateTime.now().plusMonths(1L))
                .issuedAt(LocalDateTime.now())
                .revoked(false)
                .build();
        refreshTokenRepo.save(token);
        return token.getToken();
    }

    @Override
    public RefreshToken validateRefreshToken(String token) {
        RefreshToken refToken = refreshTokenRepo.findByToken(token).orElseThrow(
                () -> new ResourceNotFoundException("Refresh Token Not Found")
        );
        if (refToken.getRevoked()) {
            throw new InvalidTokenException("Refresh Token Expired");
        }
        if (refToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Refresh Token Expired");
        }
        refToken.setLastUsedAt(LocalDateTime.now());
        refreshTokenRepo.save(refToken);
        return refToken;
    }

    @Override
    public RefreshToken rotate(String token) {
        RefreshToken refToken = refreshTokenRepo.findByToken(token).orElseThrow(
                () -> new ResourceNotFoundException("Refresh Token Not Found")
        );
        refToken.setLastUsedAt(LocalDateTime.now());
        refToken.setRevoked(true);
        refreshTokenRepo.save(refToken);

        RefreshToken rotatedToken = RefreshToken.builder()
                .user(refToken.getUser())
                .token(SecureTokenGen.generate())
                .expiresAt(LocalDateTime.now().plusMonths(1L))
                .issuedAt(LocalDateTime.now())
                .revoked(false)
                .build();
        refreshTokenRepo.save(refToken);
        return rotatedToken;
    }
}
