package com.example.petify.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConfirmChangePassRequest {
    @NotNull
    @NotBlank
    private String token;

    @NotNull
    @NotBlank
    private String newPassword;
}
