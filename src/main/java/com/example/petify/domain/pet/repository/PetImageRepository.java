package com.example.petify.domain.pet.repository;

import com.example.petify.domain.pet.model.PetImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetImageRepository extends JpaRepository<PetImage, Long> {

    List<PetImage> findByPetId(Long petId);
}
