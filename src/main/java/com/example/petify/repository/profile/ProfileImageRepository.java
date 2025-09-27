package com.example.petify.repository.profile;

import com.example.petify.model.profile.Profile;
import com.example.petify.model.profile.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfileImageRepository extends JpaRepository<ProfileImage , Long> {
    List<ProfileImage> findByProfile(Profile profile);
}
