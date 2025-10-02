package com.example.petify.dto.user.serviceprovider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderResponse {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String description;
    private String contactInfo;
}
