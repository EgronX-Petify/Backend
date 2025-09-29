package com.example.petify.service.user;

import com.example.petify.model.profile.ProfileImage;
import com.example.petify.dto.user.UpdateUserProfileRequest;
import com.example.petify.dto.user.UserProfileResponse;
import com.example.petify.model.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    User getUserById(Long id);

    UserProfileResponse getCurrentUserProfile();

    UserProfileResponse updateCurrentUserProfile(UpdateUserProfileRequest request);

    UserProfileResponse getUserProfile(Long userId);

    ProfileImage addImage(MultipartFile file);

    ProfileImage getImageById(Long imageId);

    List<ProfileImage> getImages();

    void deleteImage(Long imageId);
}