package com.example.petify.domain.pet.repository;

import com.example.petify.domain.pet.model.Pet;
import com.example.petify.domain.profile.model.POProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    

    List<Pet> findByProfile(POProfile profile);

    List<Pet> findByProfileId(Long profileId);

    Optional<Pet> findByIdAndProfileId(Long petId, Long profileId);

    long countByProfileId(Long profileId);
}