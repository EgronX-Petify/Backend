package com.example.petify.pet.service;

import com.example.petify.pet.dto.CreatePetRequest;
import com.example.petify.pet.dto.PetResponse;
import com.example.petify.pet.dto.UpdatePetRequest;
import com.example.petify.domain.profile.model.POProfile;

import java.util.List;

public interface PetService {
    

    PetResponse createPet(CreatePetRequest request, Long userId);

    List<PetResponse> getAllPets(Long userId);

    PetResponse getPetById(Long petId , Long userId);

    PetResponse updatePet(Long petId, UpdatePetRequest request, Long userId);

    void deletePet(Long petId , Long userId);

    long getPetCountByProfile( Long userId);
}