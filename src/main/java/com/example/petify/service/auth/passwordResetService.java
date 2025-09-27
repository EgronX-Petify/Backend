package com.example.petify.service.auth;

import com.example.petify.dto.auth.ChangePassTokenResponse;

public interface passwordResetService {
    public ChangePassTokenResponse ResetPassword(String email);
    public void ConfirmReset(String token, String password);
}
