package com.example.petify.security;

import com.example.petify.domain.user.model.Role;
import com.example.petify.domain.user.model.User;
import com.example.petify.domain.user.model.UserStatus;
import com.example.petify.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserInfoDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        if (user.getStatus() == UserStatus.BANNED) {
            throw new LockedException("User account is banned");
        }
        
        if (user.getStatus() == UserStatus.PENDING && user.getRole() == Role.SERVICE_PROVIDER) {
            throw new DisabledException("Account is pending approval");
        }

        return new UserInfoDetails(user);
    }
}
