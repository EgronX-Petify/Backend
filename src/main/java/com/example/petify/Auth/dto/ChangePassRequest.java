package com.example.petify.Auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePassRequest {
    @Email
    @NotNull
    @NotBlank
    private String email;
}
