package com.example.petify.service.pet.impl;

import com.example.petify.service.auth.AuthenticatedUserService;
import com.example.petify.model.profile.SPProfile;
import com.example.petify.model.service.ServiceCategory;
import com.example.petify.model.service.Services;
import com.example.petify.repository.service.ServiceRepository;
import com.example.petify.model.user.User;
import com.example.petify.repository.user.UserRepository;
import com.example.petify.exception.ResourceNotFoundException;
import com.example.petify.dto.pet.ServiceResponse;
import com.example.petify.mapper.pet.ServiceMapper;
import com.example.petify.service.pet.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;
    private final AuthenticatedUserService authenticatedUserService;
    private final UserRepository userRepository;


    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> searchServices(String searchTerm) {
        List<Services> services = serviceRepository.searchServices(searchTerm);
        return services.stream()
                .map(serviceMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> getServicesByCategory(String category) {
        ServiceCategory cat = ServiceCategory.valueOf(category.toUpperCase());
        List<Services> services = serviceRepository.findByCategory(cat);
        return services.stream()
                .map(serviceMapper::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public ServiceResponse getServiceById(Long id) {
        Services service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
        return serviceMapper.mapToResponse(service);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> getAllServices() {
        List<Services> services = serviceRepository.findAll();
        return services.stream()
                .map(serviceMapper::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> getServicesByProvider(Long providerId) {

        User user = userRepository.findById(providerId).orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + providerId));
        if(!(user.getProfile() instanceof SPProfile)){
            throw new IllegalArgumentException("Invalid Service Provider Id");
        }

        SPProfile provider = (SPProfile)user.getProfile();
        List<Services> services = serviceRepository.findByProvider(provider);
        return services.stream()
                .map(serviceMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> getServicesByProviderAndCategory(Long providerId, String category) {

        User user = userRepository.findById(providerId).orElseThrow(() -> new ResourceNotFoundException("Provider not found with id: " + providerId));
        if(!(user.getProfile() instanceof SPProfile)){
            throw new IllegalArgumentException("Invalid Service Provider Id");
        }
        SPProfile provider = (SPProfile)user.getProfile();
        ServiceCategory cat = ServiceCategory.valueOf(category.toUpperCase());

        List<Services> services = serviceRepository.findByProviderAndCategory(provider , cat);
        return services.stream()
                .map(serviceMapper::mapToResponse)
                .collect(Collectors.toList());
    }

}