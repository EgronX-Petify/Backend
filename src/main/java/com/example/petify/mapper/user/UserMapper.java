package com.example.petify.mapper.user;

import com.example.petify.dto.auth.UserResponse;
import com.example.petify.model.profile.Profile;
import com.example.petify.model.user.User;
import lombok.Builder;
import lombok.Data;

public class UserMapper {
    public static UserResponse toDto(User user) {
        Profile profile = user.getProfile();

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().toString())
                .status(user.getStatus().toString())
                .name(profile != null ? profile.getName() : null)
                .address(profile != null ? profile.getAddress() : null)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
