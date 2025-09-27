package com.example.petify.mapper.user;


import com.example.petify.model.profile.Profile;
import com.example.petify.model.profile.SPProfile;
import com.example.petify.model.user.User;

import com.example.petify.dto.user.UpdateUserProfileRequest;
import com.example.petify.dto.user.UserProfileResponse;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public UserProfileResponse toProfileResponse(Profile profile) {
        if (profile == null) {
            return null;
        }

        User user = profile.getUser();

        UserProfileResponse.UserProfileResponseBuilder builder = UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phoneNumber(profile.getPhoneNumber())
                .images(profile.getImages())
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