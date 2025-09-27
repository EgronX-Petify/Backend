package com.example.petify.auth.services;

import com.example.petify.domain.user.model.User;

public interface AuthenticatedUserService {
    public User getCurrentUser();
}
