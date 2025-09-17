package com.example.petify.user.controller;

import com.example.petify.user.dto.UpdateUserProfileRequest;
import com.example.petify.user.dto.UserProfileResponse;
import com.example.petify.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService profileService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'PET_OWNER')")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {
        UserProfileResponse profile = profileService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE_PROVIDER', 'PET_OWNER')")
    public ResponseEntity<UserProfileResponse> updateCurrentUserProfile(
            @Valid @RequestBody UpdateUserProfileRequest request) {
        UserProfileResponse updatedProfile = profileService.updateCurrentUserProfile(request);
        return ResponseEntity.ok(updatedProfile);
    }
}