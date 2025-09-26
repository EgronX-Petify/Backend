package com.example.petify.auth.services;

import com.example.petify.auth.dto.ChangePassTokenResponse;

public interface passwordResetService {
    public ChangePassTokenResponse ResetPassword(String email);
    public void ConfirmReset(String token, String password);
}
