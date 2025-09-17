package com.example.petify.user.service.impl;

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

    @Override
    public UserProfileResponse getCurrentUserProfile() {
        User user = getCurrentUser();
        Profile profile = user.getProfile();
        return profileMapper.toProfileResponse(profile);
    }

    @Override
    public UserProfileResponse updateCurrentUserProfile(UpdateUserProfileRequest request) {
        User user = getCurrentUser();
        Profile profile = user.getProfile();

        profileMapper.updateProfileFromRequest(profile, request);
        user.setProfile(profile);
        userRepository.save(user);

        return profileMapper.toProfileResponse(user.getProfile());
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        
        UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        User user = userRepository.findById(id).get();
        return user;
    }
}