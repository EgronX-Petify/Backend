package com.example.petify.Runner;


import com.example.petify.model.user.Role;
import com.example.petify.model.user.User;
import com.example.petify.model.user.UserStatus;
import com.example.petify.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String ADMIN_EMAIL = "admin@petify.com";
        String ADMIN_PASSWORD = "admin";

        userRepo.findByEmail(ADMIN_EMAIL).orElseGet( () ->
                userRepo.save(
                        User.builder()
                                .email(ADMIN_EMAIL)
                                .status(UserStatus.ACTIVE)
                                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                                .role(Role.ADMIN)
                                .build())
        );

        log.info("Admin has been initialized");
        log.info("with email: {}", ADMIN_EMAIL);
        log.info("with password: {}", ADMIN_PASSWORD);
    }
}
