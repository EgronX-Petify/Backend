package com.example.petify.service.user.impl;

import com.example.petify.dto.user.admin.AdminActionResponse;
import com.example.petify.dto.user.admin.UserStatusCountDto;
import com.example.petify.dto.user.admin.UserSummaryDto;
import com.example.petify.service.user.AdminService;
import com.example.petify.repository.product.ProductRepository;
import com.example.petify.repository.service.ServiceRepository;
import com.example.petify.model.user.Role;
import com.example.petify.model.user.User;
import com.example.petify.model.user.UserStatus;
import com.example.petify.repository.user.RefreshTokenRepository;
import com.example.petify.repository.user.UserRepository;
import com.example.petify.exception.ResourceNotFoundException;
import com.example.petify.service.notification.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ServiceRepository serviceRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public AdminActionResponse approveServiceProvider(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (user.getRole() != Role.SERVICE_PROVIDER) {
            return AdminActionResponse.builder()
                    .message("User is not a service provider")
                    .success(false)
                    .build();
        }
        
        if (user.getStatus() != UserStatus.PENDING) {
            return AdminActionResponse.builder()
                    .message("User is not in pending status")
                    .success(false)
                    .build();
        }
        
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        
        // Send welcome notification and email to the approved service provider
        notificationService.sendServiceProviderApprovedNotification(user.getProfile());
        
        return AdminActionResponse.builder()
                .message("Service provider approved successfully")
                .success(true)
                .build();
    }

    @Override
    @Transactional
    public AdminActionResponse banUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        user.setStatus(UserStatus.BANNED);
        userRepository.save(user);
        
        // Revoke all refresh tokens
        refreshTokenRepository.findByUser(user).forEach(token -> {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        });
        
        // Send ban notification and email to the user
        notificationService.sendUserBannedNotification(user.getProfile());
        
        return AdminActionResponse.builder()
                .message("User banned successfully")
                .success(true)
                .build();
    }

    @Override
    @Transactional
    public AdminActionResponse unbanUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (user.getStatus() != UserStatus.BANNED) {
            return AdminActionResponse.builder()
                    .message("User is not banned")
                    .success(false)
                    .build();
        }
        
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        
        // Send unban notification and email to the user
        notificationService.sendUserUnbannedNotification(user.getProfile());
        
        return AdminActionResponse.builder()
                .message("User unbanned successfully")
                .success(true)
                .build();
    }

    @Override
    @Transactional
    public AdminActionResponse removeProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            return AdminActionResponse.builder()
                    .message("Product not found")
                    .success(false)
                    .build();
        }
        
        productRepository.deleteById(productId);
        
        return AdminActionResponse.builder()
                .message("Product removed successfully")
                .success(true)
                .build();
    }

    @Override
    @Transactional
    public AdminActionResponse removeService(Long serviceId) {
        if (!serviceRepository.existsById(serviceId)) {
            return AdminActionResponse.builder()
                    .message("Service not found")
                    .success(false)
                    .build();
        }
        
        serviceRepository.deleteById(serviceId);
        
        return AdminActionResponse.builder()
                .message("Service removed successfully")
                .success(true)
                .build();
    }

    @Override
    public List<UserSummaryDto> getPendingServiceProviders() {
        List<User> pendingUsers = userRepository.findByStatus(UserStatus.PENDING);
        return pendingUsers.stream()
                .filter(user -> user.getRole() == Role.SERVICE_PROVIDER)
                .map(this::convertToUserSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserSummaryDto> getAllUsers(UserStatus status) {
        List<User> users = status != null ? userRepository.findByStatus(status) : userRepository.findAll();
        return users.stream()
                .map(this::convertToUserSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserStatusCountDto getUserStatusCounts() {
        Long activeCount = userRepository.countByStatus(UserStatus.ACTIVE);
        Long pendingCount = userRepository.countByStatus(UserStatus.PENDING);
        Long bannedCount = userRepository.countByStatus(UserStatus.BANNED);
        Long totalCount = userRepository.count();
        
        return UserStatusCountDto.builder()
                .activeUsers(activeCount)
                .pendingUsers(pendingCount)
                .bannedUsers(bannedCount)
                .totalUsers(totalCount)
                .build();
    }

    private UserSummaryDto convertToUserSummaryDto(User user) {
        UserSummaryDto dto = new UserSummaryDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}