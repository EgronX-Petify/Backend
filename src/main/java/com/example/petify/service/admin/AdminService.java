package com.example.petify.admin.service;

import com.example.petify.admin.dto.AdminActionResponse;
import com.example.petify.admin.dto.UserStatusCountDto;
import com.example.petify.admin.dto.UserSummaryDto;
import com.example.petify.domain.user.model.UserStatus;

import java.util.List;

public interface AdminService {
    AdminActionResponse approveServiceProvider(Long userId);
    AdminActionResponse banUser(Long userId);
    AdminActionResponse unbanUser(Long userId);
    AdminActionResponse removeProduct(Long productId);
    AdminActionResponse removeService(Long serviceId);
    List<UserSummaryDto> getPendingServiceProviders();
    List<UserSummaryDto> getAllUsers(UserStatus status);
    UserStatusCountDto getUserStatusCounts();
}