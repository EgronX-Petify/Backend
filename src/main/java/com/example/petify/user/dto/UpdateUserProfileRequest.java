package com.example.petify.user.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileRequest {
    
    @Size(max = 255, message = "Phone number must not exceed 255 characters")
    private String phoneNumber;
    
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    // sp specific
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 500, message = "Contact info must not exceed 500 characters")
    private String contactInfo;
    

}