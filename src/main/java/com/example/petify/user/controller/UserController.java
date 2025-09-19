package com.example.petify.user.controller;

import com.example.petify.domain.profile.model.ProfileImage;
import com.example.petify.user.dto.UpdateUserProfileRequest;
import com.example.petify.user.dto.UserProfileResponse;
import com.example.petify.user.service.UserService;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'PET_OWNER')")
    public ResponseEntity<UserProfileResponse> getUserProfile(@RequestParam Long userId) {
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
    public ResponseEntity<Void> deletePetImage(@PathVariable Long imageId)
    {
        userService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}