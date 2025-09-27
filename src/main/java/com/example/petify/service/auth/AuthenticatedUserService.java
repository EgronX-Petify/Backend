package com.example.petify.service.auth;

import com.example.petify.model.user.User;

public interface AuthenticatedUserService {
    public User getCurrentUser();
}
