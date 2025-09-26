package com.example.petify.domain.profile.repository;

import com.example.petify.domain.profile.model.Profile;
import com.example.petify.domain.profile.model.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileImageRepository extends JpaRepository<ProfileImage , Long> {
    List<ProfileImage> findByProfile(Profile profile);
}
