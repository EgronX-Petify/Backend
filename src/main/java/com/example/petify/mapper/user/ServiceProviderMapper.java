package com.example.petify.mapper.user;

import com.example.petify.dto.user.serviceprovider.ServiceProviderResponse;
import com.example.petify.model.profile.Profile;
import com.example.petify.model.profile.SPProfile;
import com.example.petify.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class ServiceProviderMapper {
    
    public ServiceProviderResponse toServiceProviderResponse(User user) {
        Profile profile = user.getProfile();
        
        if (!(profile instanceof SPProfile)) {
            throw new IllegalArgumentException("User is not a service provider");
        }
        
        SPProfile spProfile = (SPProfile) profile;
        
        return ServiceProviderResponse.builder()
                .id(user.getId())
                .name(profile.getName())
                .email(user.getEmail())
                .phoneNumber(profile.getPhoneNumber())
                .address(profile.getAddress())
                .description(spProfile.getDescription())
                .contactInfo(spProfile.getContactInfo())
                .build();
    }
}
