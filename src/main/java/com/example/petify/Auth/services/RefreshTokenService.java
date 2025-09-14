package com.example.petify.Auth.services;


import com.example.petify.domain.user.model.RefreshToken;
import com.example.petify.security.UserInfoDetails;

public interface RefreshTokenService {
    public String generateRefreshToken(Long userId);
    public RefreshToken validateRefreshToken(String refreshToken);
    public RefreshToken rotate(String token);
}
