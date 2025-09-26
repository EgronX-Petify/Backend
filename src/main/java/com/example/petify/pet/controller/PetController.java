package com.example.petify.pet.controller;

import com.example.petify.domain.pet.model.PetImage;
import com.example.petify.pet.dto.CreatePetRequest;
import com.example.petify.pet.dto.PetResponse;
import com.example.petify.pet.dto.UpdatePetRequest;
import com.example.petify.pet.service.PetService;
import com.example.petify.domain.profile.model.POProfile;
import com.example.petify.domain.profile.model.Profile;
import com.example.petify.security.UserInfoDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/me/pet")
@RequiredArgsConstructor
public class PetController {
    
    private final PetService petService;

    @PostMapping
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<PetResponse> createPet(
            @Valid @RequestBody CreatePetRequest request) {

        PetResponse response = petService.createPet(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    

    @GetMapping
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<List<PetResponse>> getAllPets() {

        List<PetResponse> pets = petService.getAllPets();
        return ResponseEntity.ok(pets);
    }
    

    @GetMapping("/{petId}")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<PetResponse> getPetById(@PathVariable Long petId) {

        PetResponse response = petService.getPetById(petId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{petId}")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<PetResponse> updatePet(
            @PathVariable Long petId,
            @Valid @RequestBody UpdatePetRequest request) {

        PetResponse response = petService.updatePet(petId, request);
        return ResponseEntity.ok(response);
    }
    

    @DeleteMapping("/{petId}")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<Void> deletePet(@PathVariable Long petId) {

        petService.deletePet(petId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<Long> getPetCount() {

        long count = petService.getPetCountByProfile();
        return ResponseEntity.ok(count);
    }


    @PostMapping("/{petId}/image")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<PetImage> uploadImage(
            @RequestParam MultipartFile file,
            @RequestParam Long petId) {
        PetImage image = petService.addImage(petId , file);
        return ResponseEntity.ok(image);
    }

    @GetMapping("/{petId}/image/{imageId}")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<PetImage> getImage(
            @PathVariable Long petId,
            @PathVariable Long imageId) {
        PetImage image = petService.getImageById(petId , imageId);
        return ResponseEntity.ok(image);
    }

    @GetMapping("/{petId}/image")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<List<PetImage>> getPetImages(@PathVariable Long petId){
        List<PetImage> images = petService.getPetImages(petId);
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/{petId}/image/{imageId}")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<Void> deletePetImage(
            @PathVariable Long petId,
            @PathVariable Long imageId)
    {
        petService.deletePetImage(petId , imageId);
        return ResponseEntity.noContent().build();
    }
}