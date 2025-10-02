package com.example.petify.service.pet.impl;

import com.example.petify.service.auth.AuthenticatedUserService;
import com.example.petify.model.pet.PetImage;
import com.example.petify.repository.pet.PetImageRepository;
import com.example.petify.model.profile.Profile;
import com.example.petify.model.user.User;
import com.example.petify.exception.FileStorageException;
import com.example.petify.dto.pet.CreatePetRequest;
import com.example.petify.dto.pet.PetResponse;
import com.example.petify.dto.pet.UpdatePetRequest;
import com.example.petify.model.pet.Pet;
import com.example.petify.repository.pet.PetRepository;
import com.example.petify.mapper.pet.PetMapper;
import com.example.petify.service.pet.PetService;
import com.example.petify.model.profile.POProfile;
import com.example.petify.exception.ResourceNotFoundException;
import com.example.petify.utils.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PetServiceImpl implements PetService {
    
    private final PetRepository petRepository;
    private final AuthenticatedUserService authenticatedUserService;
    private final PetMapper petMapper;
    private final PetImageRepository petImageRepository;

    @Override
    public PetResponse createPet(CreatePetRequest request) {
        User user = authenticatedUserService.getCurrentUser();

        POProfile profile = getPOProfile(user);

        Pet pet = Pet.builder()
                .name(request.getName())
                .species(request.getSpecies())
                .breed(request.getBreed())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .profile(profile)
                .build();
        
        Pet savedPet = petRepository.save(pet);
        return petMapper.mapToResponse(savedPet);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PetResponse> getAllPets() {

        User user = authenticatedUserService.getCurrentUser();
        POProfile profile = getPOProfile(user);
        List<Pet> pets = petRepository.findByProfile(profile);

        pets.forEach(pet -> {
            pet.getImages().forEach(image -> {
                if (image.getFilePath() != null) {
                    image.setData(FileStorageUtil.loadFile(image.getFilePath()));
                }
            });
        });

        return pets.stream()
                .map(petMapper::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public PetResponse getPetById(Long petId) {

        User user = authenticatedUserService.getCurrentUser();
        POProfile profile = getPOProfile(user);

        Pet pet = petRepository.findByIdAndProfileId(petId, profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("error while fetching the pet with id: " + petId));
        
        pet.getImages().forEach(image -> {
            if (image.getFilePath() != null) {
                image.setData(FileStorageUtil.loadFile(image.getFilePath()));
            }
        });
        
        return petMapper.mapToResponse(pet);
    }
    
    @Override
    public PetResponse updatePet(Long petId, UpdatePetRequest request) {
        User user = authenticatedUserService.getCurrentUser();
        POProfile profile = getPOProfile(user);

        Pet pet = petRepository.findByIdAndProfileId(petId, profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with ID: " + petId));
        
        petMapper.updatePetFromRequest(pet , request);
        
        Pet updatedPet = petRepository.save(pet);
        return petMapper.mapToResponse(updatedPet);
    }
    
    @Override
    public void deletePet(Long petId) {
        User user = authenticatedUserService.getCurrentUser();
        POProfile profile = getPOProfile(user);

        Pet pet = petRepository.findByIdAndProfileId(petId, profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found with ID: " + petId));
        
        petRepository.delete(pet);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getPetCountByProfile() {
        User user = authenticatedUserService.getCurrentUser();
        POProfile profile = getPOProfile(user);

        return petRepository.countByProfileId(profile.getId());
    }

    @Override
    public PetImage addImage(Long petId , MultipartFile file) {
        User user = authenticatedUserService.getCurrentUser();
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + petId));
        if(!pet.getProfile().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not the owner of this pet");
        }
        
        String filePath = FileStorageUtil.saveFile(file, "pets");
        
        PetImage image = PetImage.builder()
                .contentType(file.getContentType())
                .name(file.getOriginalFilename())
                .filePath(filePath)
                .pet(pet)
                .build();

        return petImageRepository.save(image);
    }

    @Override
    public PetImage getImageById(Long petId, Long imageId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + petId));
        PetImage image = petImageRepository.findById(imageId).orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));
        
        if (image.getFilePath() != null) {
            image.setData(FileStorageUtil.loadFile(image.getFilePath()));
        }
        
        return image;
    }

    @Override
    public List<PetImage> getPetImages(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + petId));
        List<PetImage> images = petImageRepository.findByPetId(petId);
        
        images.forEach(image -> {
            if (image.getFilePath() != null) {
                image.setData(FileStorageUtil.loadFile(image.getFilePath()));
            }
        });
        
        return images;
    }

    @Override
    public void deletePetImage(Long petId, Long imageId) {
        User user = authenticatedUserService.getCurrentUser();
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new ResourceNotFoundException("Pet not found with id: " + petId));
        if(!pet.getProfile().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("User is not the owner of this pet");
        }
        
        PetImage image = petImageRepository.findById(imageId).orElse(null);
        if (image != null && image.getFilePath() != null) {
            FileStorageUtil.deleteFile(image.getFilePath());
        }
        
        petImageRepository.deleteById(imageId);
    }


    private POProfile getPOProfile(User user) {
        Profile profile = user.getProfile();
        if (!(profile instanceof POProfile)) {
            throw new IllegalArgumentException("User is not a Pet Owner");
        }
        return (POProfile) profile;
    }

}