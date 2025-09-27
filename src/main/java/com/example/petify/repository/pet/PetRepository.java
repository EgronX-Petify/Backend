package com.example.petify.repository.pet;

import com.example.petify.model.pet.Pet;
import com.example.petify.model.profile.POProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    

    List<Pet> findByProfile(POProfile profile);


    Optional<Pet> findByIdAndProfileId(Long petId, Long profileId);

    long countByProfileId(Long profileId);
}