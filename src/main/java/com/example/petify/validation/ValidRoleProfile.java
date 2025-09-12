package com.example.petify.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoleProfileValidator.class)
public @interface ValidRoleProfile {
    String message() default "Role and profile type do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}