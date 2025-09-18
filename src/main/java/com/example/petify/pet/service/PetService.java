package com.example.petify.pet.service;

import com.example.petify.pet.dto.CreatePetRequest;
import com.example.petify.pet.dto.PetResponse;
import com.example.petify.pet.dto.UpdatePetRequest;
import com.example.petify.domain.profile.model.POProfile;

import java.util.List;

public interface PetService {
    

    PetResponse createPet(CreatePetRequest request);

    List<PetResponse> getAllPets();

    PetResponse getPetById(Long petId);

    PetResponse updatePet(Long petId, UpdatePetRequest request);

    void deletePet(Long petId);

    long getPetCountByProfile();
}