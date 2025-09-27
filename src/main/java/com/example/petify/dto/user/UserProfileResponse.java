package com.example.petify.user.dto;

import com.example.petify.domain.profile.model.ProfileImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {


    // Common fields for all profile types
    private Long id;
    private String name;
    private String email;
    private String role;
    private Set<ProfileImage> images;
    private String phoneNumber;
    private String address;


    // SP specific
    private String description;
    private String contactInfo;



}