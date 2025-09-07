package com.example.petify.exception;

public class UsernameAlreadyExists extends RuntimeException {
    public UsernameAlreadyExists(String email) {
        super("User with email " + email + " already exists");
    }
}
