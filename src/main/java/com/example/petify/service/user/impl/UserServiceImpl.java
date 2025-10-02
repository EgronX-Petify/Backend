package com.example.petify.service.user.impl;

import com.example.petify.service.auth.AuthenticatedUserService;
import com.example.petify.model.profile.Profile;
import com.example.petify.model.profile.ProfileImage;
import com.example.petify.repository.profile.ProfileImageRepository;
import com.example.petify.service.notification.NotificationService;
import com.example.petify.model.user.User;
import com.example.petify.repository.user.UserRepository;
import com.example.petify.exception.FileStorageException;
import com.example.petify.exception.ResourceNotFoundException;
import com.example.petify.dto.user.UpdateUserProfileRequest;
import com.example.petify.dto.user.UserProfileResponse;
import com.example.petify.mapper.user.ProfileMapper;
import com.example.petify.service.user.UserService;
import com.example.petify.utils.FileStorageUtil;
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
        
        profile.getImages().forEach(image -> {
            if (image.getFilePath() != null) {
                image.setData(FileStorageUtil.loadFile(image.getFilePath()));
            }
        });
        
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

        profile.getImages().forEach(image -> {
            if (image.getFilePath() != null) {
                image.setData(FileStorageUtil.loadFile(image.getFilePath()));
            }
        });

        return profileMapper.toProfileResponse(user.getProfile());
    }

    @Override
    public UserProfileResponse getUserProfile(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        user.getProfile().getImages().forEach(image -> {
            if (image.getFilePath() != null) {
                image.setData(FileStorageUtil.loadFile(image.getFilePath()));
            }
        });
        
        return profileMapper.toProfileResponse(user.getProfile());
    }

    @Override
    public ProfileImage addImage(MultipartFile file) {
        User user = authenticatedUserService.getCurrentUser();
        Profile profile = user.getProfile();

        try {
            String filePath = FileStorageUtil.saveFile(file, "profiles");
            
            ProfileImage image = ProfileImage.builder()
                    .profile(profile)
                    .name(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .filePath(filePath)
                    .build();
            return profileImageRepository.save(image);

        } catch (IOException e) {
            throw new FileStorageException("Could not store the image");
        }
    }

    @Override
    public ProfileImage getImageById(Long imageId) {
        ProfileImage image = profileImageRepository.findById(imageId).orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));
        
        if (image.getFilePath() != null) {
            image.setData(FileStorageUtil.loadFile(image.getFilePath()));
        }
        
        return image;
    }

    @Override
    public List<ProfileImage> getImages() {
        User user = authenticatedUserService.getCurrentUser();
        Profile profile = user.getProfile();
        List<ProfileImage> images = profileImageRepository.findByProfile(profile);
        
        images.forEach(image -> {
            if (image.getFilePath() != null) {
                image.setData(FileStorageUtil.loadFile(image.getFilePath()));
            }
        });
        
        return images;
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

        if (image.getFilePath() != null) {
            FileStorageUtil.deleteFile(image.getFilePath());
        }

        profile.getImages().remove(image);
        profileImageRepository.deleteById(imageId);
        profileImageRepository.flush();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with this" + id + "id Not Found")
        );
    }

}