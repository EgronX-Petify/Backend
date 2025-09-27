package com.example.petify.service.pet;

import com.example.petify.model.pet.PetImage;
import com.example.petify.dto.pet.CreatePetRequest;
import com.example.petify.dto.pet.PetResponse;
import com.example.petify.dto.pet.UpdatePetRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PetService {
    

    PetResponse createPet(CreatePetRequest request);

    List<PetResponse> getAllPets();

    PetResponse getPetById(Long petId);

    PetResponse updatePet(Long petId, UpdatePetRequest request);

    void deletePet(Long petId);

    long getPetCountByProfile();

    PetImage addImage(Long petId , MultipartFile file);

    PetImage getImageById(Long petId, Long imageId);

    List<PetImage> getPetImages(Long petId);

    void deletePetImage(Long petId, Long imageId);
}