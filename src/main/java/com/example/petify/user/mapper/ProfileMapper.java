package com.example.petify.user.mapper;


import com.example.petify.domain.profile.model.Profile;
import com.example.petify.domain.profile.model.SPProfile;
import com.example.petify.domain.user.model.User;

import com.example.petify.user.dto.UpdateUserProfileRequest;
import com.example.petify.user.dto.UserProfileResponse;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public UserProfileResponse toProfileResponse(Profile profile) {
        if (profile == null) {
            return null;
        }

        User user = profile.getUser();

        UserProfileResponse.UserProfileResponseBuilder builder = UserProfileResponse.builder()
                .email(user.getEmail())
                .phoneNumber(profile.getPhoneNumber())
                .imageUrls(profile.getImageUrls())
                .role(user.getRole().name())
                .name(profile.getName())
                .address(profile.getAddress());

        if (profile instanceof SPProfile spProfile) {
            builder.contactInfo(spProfile.getContactInfo())
                    .description(spProfile.getDescription());
        }

        return builder.build();
    }

    public void updateProfileFromRequest(Profile profile, UpdateUserProfileRequest request) {
        if (request == null) {
            return;
        }

        //common fields
        if (request.getName() != null) {
            profile.setName(request.getName());
        }

        if (request.getPhoneNumber() != null) {
            profile.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getAddress() != null) {
            profile.setAddress(request.getAddress());
        }

        if (profile instanceof SPProfile) {
            SPProfile spProfile = (SPProfile) profile;
            if (request.getDescription() != null) {
                spProfile.setDescription(request.getDescription());
            }
            if (request.getContactInfo() != null) {
                spProfile.setContactInfo(request.getContactInfo());
            }
        }
    }
}