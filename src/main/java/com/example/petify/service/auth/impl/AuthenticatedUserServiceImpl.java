package com.example.petify.auth.services.impl;

import com.example.petify.auth.services.AuthenticatedUserService;
import com.example.petify.domain.user.model.User;
import com.example.petify.domain.user.repository.UserRepository;
import com.example.petify.security.UserInfoDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticatedUserServiceImpl implements AuthenticatedUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
        Long id = userDetails.getId();
        User user = userRepository.findById(id).get();
        return user;
    }
}
