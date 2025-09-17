package com.example.petify.pet.controller;

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

import java.util.List;

@RestController
@RequestMapping("/api/user/{userId}/pet")
@RequiredArgsConstructor
public class PetController {
    
    private final PetService petService;

    @PostMapping
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<PetResponse> createPet(
            @Valid @RequestBody CreatePetRequest request ,
            @PathVariable Long userId) {

        PetResponse response = petService.createPet(request , userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    

    @GetMapping
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<List<PetResponse>> getAllPets(@PathVariable Long userId) {

        List<PetResponse> pets = petService.getAllPets(userId);
        return ResponseEntity.ok(pets);
    }
    

    @GetMapping("/{petId}")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<PetResponse> getPetById(
            @PathVariable Long petId,
            @PathVariable Long userId) {

        PetResponse response = petService.getPetById(petId, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{petId}")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<PetResponse> updatePet(
            @PathVariable Long petId,
            @Valid @RequestBody UpdatePetRequest request,
            @PathVariable Long userId) {

        PetResponse response = petService.updatePet(petId, request, userId);
        return ResponseEntity.ok(response);
    }
    

    @DeleteMapping("/{petId}")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<Void> deletePet(
            @PathVariable Long petId,
            @PathVariable Long userId) {

        petService.deletePet(petId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('PET_OWNER')")
    public ResponseEntity<Long> getPetCount(@PathVariable Long userId) {

        long count = petService.getPetCountByProfile(userId);
        return ResponseEntity.ok(count);
    }


}