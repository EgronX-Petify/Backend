package com.example.petify.pet.mapper;


import com.example.petify.domain.pet.model.Pet;
import com.example.petify.pet.dto.PetResponse;
import com.example.petify.pet.dto.UpdatePetRequest;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

    public PetResponse mapToResponse(Pet pet) {
        return PetResponse.builder()
                .id(pet.getId())
                .name(pet.getName())
                .species(pet.getSpecies())
                .breed(pet.getBreed())
                .gender(pet.getGender())
                .dateOfBirth(pet.getDateOfBirth())
                .images(pet.getImages())
                .build();
    }

    public void updatePetFromRequest(Pet pet, UpdatePetRequest request) {
        if (request.getName() != null) {
            pet.setName(request.getName());
        }
        if (request.getSpecies() != null) {
            pet.setSpecies(request.getSpecies());
        }
        if (request.getBreed() != null) {
            pet.setBreed(request.getBreed());
        }
        if (request.getGender() != null) {
            pet.setGender(request.getGender());
        }
        if (request.getDateOfBirth() != null) {
            pet.setDateOfBirth(request.getDateOfBirth());
        }
    }
}
