package com.example.petify.domain.PasswordReset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    public Optional<PasswordResetToken> findByToken(UUID token);

    public List<PasswordResetToken> findByUserId(Long userId);

    @Query("SELECT p FROM PasswordResetToken p WHERE p.user.email = :email")
    public PasswordResetToken findByUserEmail(@Param("email") Long email);
}
