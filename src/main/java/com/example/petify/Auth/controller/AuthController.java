package com.example.petify.Auth.controller;


import com.example.petify.Auth.dto.*;
import com.example.petify.Auth.services.AuthenticationService;
import com.example.petify.Auth.services.RefreshTokenService;
import com.example.petify.Auth.services.passwordResetService;
import com.example.petify.common.utils.CookieUtils;
import com.example.petify.domain.user.model.RefreshToken;
import com.example.petify.config.AppConfig;
import com.example.petify.security.UserInfoDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AppConfig appConfig;
    private final AuthenticationService authenticationService;
    private final passwordResetService passwordResetService;
    private final RefreshTokenService refreshTokenService;

    private final String refreshTokenCookieName = "refreshToken";

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(HttpServletResponse response, @Valid @RequestBody LoginRequest loginRequest) {
        UserInfoDetails userDetails = (UserInfoDetails) authenticationService.authenticate(
                loginRequest.getEmail(),
                loginRequest.getPassword());

        String token = authenticationService.generateAccessToken(userDetails);

        String refreshToken = refreshTokenService.generateRefreshToken(userDetails.getId());

        Cookie refreshTokenCookie = CookieUtils.createCookie(
                refreshTokenCookieName, refreshToken, "/", appConfig.getRefreshExpirationSec());

        response.addCookie(refreshTokenCookie);

        LoginResponse loginResponse = LoginResponse.builder()
                .token(token)
                .build();
        
        return ResponseEntity.status(HttpStatus.OK).body(
            loginResponse
        );
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        SignupResponse signupResponse = authenticationService.registerUser(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                signupResponse
        );
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtils.getCookieValue(request.getCookies(), refreshTokenCookieName);

        RefreshToken oldRefToken = refreshTokenService.validateRefreshToken(refreshToken);
        RefreshToken rotatedToken = refreshTokenService.rotate(oldRefToken.getToken());

        UserInfoDetails userDetails = new UserInfoDetails(rotatedToken.getUser());
        String accessToken = authenticationService.generateAccessToken(userDetails);

        // set the cookie
        Cookie cookie = CookieUtils.createCookie(
                refreshTokenCookieName, rotatedToken.getToken(), "/", appConfig.getRefreshExpirationSec());
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK).body(
                LoginResponse.builder()
                        .token(accessToken)
                        .build()
        );
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = CookieUtils.createCookie(
                refreshTokenCookieName, null, "/", 0);
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK).body("Logged out");
    }


    @PostMapping(value = "/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ChangePassRequest req) {
        ChangePassTokenResponse resp = passwordResetService.ResetPassword(req.getEmail());
        if (appConfig.isDev()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    resp
            );
        }else {
            return ResponseEntity.status(HttpStatus.CREATED).body("Token has been sent to your email successfully");
        }

    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ConfirmChangePassRequest req) {
        passwordResetService.ConfirmReset(req.getToken(), req.getNewPassword());

        Map <String, Object> resp = new HashMap<>();
        resp.put("message", "Password has been changed successfully");
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(resp);
    }
}
