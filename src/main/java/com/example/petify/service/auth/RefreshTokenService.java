package com.example.petify.service.auth;


import com.example.petify.model.user.RefreshToken;

public interface RefreshTokenService {
    public String generateRefreshToken(Long userId);
    public RefreshToken validateRefreshToken(String refreshToken);
    public RefreshToken rotate(String token);
}
