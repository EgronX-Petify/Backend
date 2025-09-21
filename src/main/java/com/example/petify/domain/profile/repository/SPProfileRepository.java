package com.example.petify.domain.profile.repository;

import com.example.petify.domain.profile.model.SPProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SPProfileRepository extends JpaRepository<SPProfile, Long> {
}
