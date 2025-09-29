package com.example.petify.dto.auth;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String status;
    private String address;
    private LocalDateTime createdAt;

    @Data
    @Builder
    public static class UserProfile {
        private byte[][] images;
        private String contactInfo;
        private String description;
    }


}
