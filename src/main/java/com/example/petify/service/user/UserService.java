package com.example.petify.service.user;

import com.example.petify.model.profile.ProfileImage;
import com.example.petify.dto.user.UpdateUserProfileRequest;
import com.example.petify.dto.user.UserProfileResponse;
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