package com.example.petify.user.service;

import com.example.petify.user.dto.UpdateUserProfileRequest;
import com.example.petify.user.dto.UserProfileResponse;

public interface UserService {

    UserProfileResponse getCurrentUserProfile();

    UserProfileResponse updateCurrentUserProfile(UpdateUserProfileRequest request);
}