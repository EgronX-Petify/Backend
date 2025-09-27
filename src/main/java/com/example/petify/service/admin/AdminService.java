package com.example.petify.service.admin;

import com.example.petify.dto.admin.AdminActionResponse;
import com.example.petify.dto.admin.UserStatusCountDto;
import com.example.petify.dto.admin.UserSummaryDto;
import com.example.petify.model.user.UserStatus;

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