package com.example.petify.service.pet.impl;

import com.example.petify.service.auth.AuthenticatedUserService;
import com.example.petify.model.profile.SPProfile;
import com.example.petify.model.service.ServiceCategory;
import com.example.petify.model.service.Services;
import com.example.petify.repository.service.ServiceRepository;
import com.example.petify.model.user.User;
import com.example.petify.repository.user.UserRepository;
import com.example.petify.exception.ResourceNotFoundException;
import com.example.petify.dto.pet.CreateServiceRequest;
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
    public ServiceResponse createService(CreateServiceRequest request) {

        User user = authenticatedUserService.getCurrentUser();

        SPProfile provider = (SPProfile)user.getProfile();
        Services service = Services.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .notes(request.getNotes())
                .provider(provider)
                .build();

        service = serviceRepository.save(service);
        return serviceMapper.mapToResponse(service);
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





    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> getMyServices(){
        User user = authenticatedUserService.getCurrentUser();
        SPProfile provider = (SPProfile)user.getProfile();

        List<Services> services = serviceRepository.findByProvider(provider);
        return services.stream()
                .map(serviceMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ServiceResponse updateService(Long serviceId, CreateServiceRequest request) {

        Services service = checkAuthorityForUpdatingServices(serviceId);


        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setCategory(request.getCategory());
        service.setPrice(request.getPrice());
        service.setNotes(request.getNotes());

        service = serviceRepository.save(service);
        return serviceMapper.mapToResponse(service);
    }

    @Override
    public void deleteService(Long serviceId) {

        Services service = checkAuthorityForUpdatingServices(serviceId);
        serviceRepository.delete(service);
    }


    private Services checkAuthorityForUpdatingServices(Long serviceId) {

        var user = authenticatedUserService.getCurrentUser();

        Services service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + serviceId));

        if (user.getId() != service.getProvider().getUser().getId()) {
            throw new IllegalArgumentException("User is not the owner of this service");
        }

        return service;
    }

}