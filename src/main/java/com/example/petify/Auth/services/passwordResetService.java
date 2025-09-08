package com.example.petify.Auth.services;

import com.example.petify.Auth.dto.ChangePassTokenResponse;

public interface passwordResetService {
    public ChangePassTokenResponse ResetPassword(String email);
    public void ConfirmReset(String token, String password);
}
