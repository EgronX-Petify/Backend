package com.example.petify.controller.user;

import com.example.petify.model.profile.ProfileImage;
import com.example.petify.dto.user.UpdateUserProfileRequest;
import com.example.petify.dto.user.UserProfileResponse;
import com.example.petify.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'PET_OWNER')")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {
        UserProfileResponse profile = userService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'PET_OWNER')")
    public ResponseEntity<UserProfileResponse> updateCurrentUserProfile(
            @Valid @RequestBody UpdateUserProfileRequest request) {
        UserProfileResponse updatedProfile = userService.updateCurrentUserProfile(request);
        return ResponseEntity.ok(updatedProfile);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long userId) {
        UserProfileResponse profile = userService.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }


    @PostMapping("/me/image")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'PET_OWNER')")
    public ResponseEntity<ProfileImage> uploadImage(@RequestParam MultipartFile file) {
        ProfileImage image = userService.addImage(file);
        return ResponseEntity.ok(image);
    }

    @GetMapping("me/image/{imageId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'PET_OWNER')")
    public ResponseEntity<ProfileImage> getImage(@PathVariable Long imageId) {
        ProfileImage image = userService.getImageById(imageId);
        return ResponseEntity.ok(image);
    }

    @GetMapping("/me/image")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'PET_OWNER')")
    public ResponseEntity<List<ProfileImage>> getImages(){
        List<ProfileImage> images = userService.getImages();
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/me/image/{imageId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'PET_OWNER')")
    public ResponseEntity<Void> deleteProfileImage(@PathVariable Long imageId)
    {
        userService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}