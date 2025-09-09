package com.example.petify.validation;

import com.example.petify.domain.profile.model.POProfile;
import com.example.petify.domain.profile.model.SPProfile;
import com.example.petify.domain.user.model.Role;
import com.example.petify.domain.user.model.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleProfileValidator implements ConstraintValidator<ValidRoleProfile, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext ctx) {
        if (user.getRole() == Role.PET_OWNER && !(user.getProfile() instanceof POProfile)) return false;
        if (user.getRole() == Role.SERVICE_PROVIDER && !(user.getProfile() instanceof SPProfile)) return false;
        return true;
    }
}
