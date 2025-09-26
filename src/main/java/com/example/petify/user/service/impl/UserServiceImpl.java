package com.example.petify.user.service.impl;

import com.example.petify.auth.services.AuthenticatedUserService;
import com.example.petify.domain.profile.model.Profile;
import com.example.petify.domain.profile.model.ProfileImage;
import com.example.petify.domain.profile.repository.ProfileImageRepository;
import com.example.petify.notification.service.NotificationService;
import com.example.petify.domain.user.model.User;
import com.example.petify.domain.user.repository.UserRepository;
import com.example.petify.exception.FileStorageException;
import com.example.petify.exception.ResourceNotFoundException;
import com.example.petify.user.dto.UpdateUserProfileRequest;
import com.example.petify.user.dto.UserProfileResponse;
import com.example.petify.user.mapper.ProfileMapper;
import com.example.petify.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final ProfileMapper profileMapper;
    private final UserRepository userRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final ProfileImageRepository profileImageRepository;
    private final NotificationService notificationService;

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


        notificationService.sendProfileUpdateNotification(profile);


        return profileMapper.toProfileResponse(user.getProfile());
    }

    @Override
    public UserProfileResponse getUserProfile(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return profileMapper.toProfileResponse(user.getProfile());
    }

    @Override
    public ProfileImage addImage(MultipartFile file) {
        User user = authenticatedUserService.getCurrentUser();
        Profile profile = user.getProfile();

        try {
            ProfileImage image = ProfileImage.builder()
                    .profile(profile)
                    .name(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .data(file.getBytes())
                    .build();
            return profileImageRepository.save(image);

        } catch (IOException e) {
            throw new FileStorageException("Could not store the image");
        }
    }

    @Override
    public ProfileImage getImageById(Long imageId) {
        ProfileImage image = profileImageRepository.findById(imageId).orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));
        return image;
    }

    @Override
    public List<ProfileImage> getImages() {
        User user = authenticatedUserService.getCurrentUser();
        Profile profile = user.getProfile();
        return profileImageRepository.findByProfile(profile);
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) {
        User user = authenticatedUserService.getCurrentUser();
        Profile profile = user.getProfile();

        ProfileImage image = profileImageRepository.findById(imageId).orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));
        if(!image.getProfile().getId().equals(profile.getId())) {
            throw new IllegalArgumentException("User is not the owner of this image");
        }

        profile.getImages().remove(image);
        profileImageRepository.deleteById(imageId);
        profileImageRepository.flush();
    }

}