package com.example.petify.repository.user;

import com.example.petify.model.user.RefreshToken;
import com.example.petify.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Query("SELECT T FROM RefreshToken T WHERE T.token = :token")
    public Optional<RefreshToken> findByToken(@Param("token") String token);
    
    List<RefreshToken> findByUser(User user);
}
