package com.example.petify.repository.profile;

import com.example.petify.model.profile.SPProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SPProfileRepository extends JpaRepository<SPProfile, Long> {
}
