package com.example.petify.user.service.impl;

import com.example.petify.auth.services.AuthenticatedUserService;
import com.example.petify.auth.services.AuthenticationService;
import com.example.petify.domain.profile.model.Profile;
import com.example.petify.domain.user.model.User;
import com.example.petify.domain.user.repository.UserRepository;
import com.example.petify.security.UserInfoDetails;
import com.example.petify.user.dto.UpdateUserProfileRequest;
import com.example.petify.user.dto.UserProfileResponse;
import com.example.petify.user.mapper.ProfileMapper;
import com.example.petify.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final ProfileMapper profileMapper;
    private final UserRepository userRepository;
    private final AuthenticatedUserService authenticatedUserService;

    @Override
    public UserProfileResponse getCurrentUserProfile() {
        User user = authenticatedUserService.getCurrentUser();
        Profile profile = user.getProfile();
        return profileMapper.toProfileResponse(profile);
    }

    @Override
    public UserProfileResponse updateCurrentUserProfile(UpdateUserProfileRequest request) {
        User user = authenticatedUserService.getCurrentUser();
        Profile profile = user.getProfile();

        profileMapper.updateProfileFromRequest(profile, request);
        user.setProfile(profile);
        userRepository.save(user);

        return profileMapper.toProfileResponse(user.getProfile());
    }


}