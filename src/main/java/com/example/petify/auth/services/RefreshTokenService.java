package com.example.petify.auth.services;


import com.example.petify.domain.user.model.RefreshToken;

public interface RefreshTokenService {
    public String generateRefreshToken(Long userId);
    public RefreshToken validateRefreshToken(String refreshToken);
    public RefreshToken rotate(String token);
}
