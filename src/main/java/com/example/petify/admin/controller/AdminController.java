package com.example.petify.admin.controller;

import com.example.petify.admin.dto.AdminActionResponse;
import com.example.petify.admin.dto.UserStatusCountDto;
import com.example.petify.admin.dto.UserSummaryDto;
import com.example.petify.admin.service.AdminService;
import com.example.petify.domain.user.model.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/approve-service-provider/{userId}")
    public ResponseEntity<AdminActionResponse> approveServiceProvider(@PathVariable Long userId) {
        AdminActionResponse response = adminService.approveServiceProvider(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/ban-user/{userId}")
    public ResponseEntity<AdminActionResponse> banUser(@PathVariable Long userId) {
        AdminActionResponse response = adminService.banUser(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/unban-user/{userId}")
    public ResponseEntity<AdminActionResponse> unbanUser(@PathVariable Long userId) {
        AdminActionResponse response = adminService.unbanUser(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove-product/{productId}")
    public ResponseEntity<AdminActionResponse> removeProduct(@PathVariable Long productId) {
        AdminActionResponse response = adminService.removeProduct(productId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove-service/{serviceId}")
    public ResponseEntity<AdminActionResponse> removeService(@PathVariable Long serviceId) {
        AdminActionResponse response = adminService.removeService(serviceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending-service-providers")
    public ResponseEntity<List<UserSummaryDto>> getPendingServiceProviders() {
        List<UserSummaryDto> pendingProviders = adminService.getPendingServiceProviders();
        return ResponseEntity.ok(pendingProviders);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserSummaryDto>> getAllUsers(@RequestParam(required = false) UserStatus status) {
        List<UserSummaryDto> users = adminService.getAllUsers(status);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user-counts")
    public ResponseEntity<UserStatusCountDto> getUserStatusCounts() {
        UserStatusCountDto counts = adminService.getUserStatusCounts();
        return ResponseEntity.ok(counts);
    }
}