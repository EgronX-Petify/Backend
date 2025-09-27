package com.example.petify.model.user;

public enum Role {
    ADMIN,
    SERVICE_PROVIDER,
    PET_OWNER;

    public static Role getRole(String role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        try {
            return Role.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

}
