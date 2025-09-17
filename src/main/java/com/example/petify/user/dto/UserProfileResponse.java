package com.example.petify.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<String> imageUrls;
    private String phoneNumber;
    private String address;


    // SP specific
    private String description;
    private String contactInfo;



}