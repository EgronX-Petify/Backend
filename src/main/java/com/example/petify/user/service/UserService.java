package com.example.petify.user.service;

import com.example.petify.domain.profile.model.ProfileImage;
import com.example.petify.user.dto.UpdateUserProfileRequest;
import com.example.petify.user.dto.UserProfileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserProfileResponse getCurrentUserProfile();

    UserProfileResponse updateCurrentUserProfile(UpdateUserProfileRequest request);

    UserProfileResponse getUserProfile(Long userId);

    ProfileImage addImage(MultipartFile file);

    ProfileImage getImageById(Long imageId);

    List<ProfileImage> getImages();

    void deleteImage(Long imageId);
}