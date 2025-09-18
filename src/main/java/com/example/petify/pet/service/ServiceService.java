package com.example.petify.pet.service;

import com.example.petify.pet.dto.CreateServiceRequest;
import com.example.petify.pet.dto.ServiceResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ServiceService {
    
    List<ServiceResponse> searchServices(String searchTerm);
    
    List<ServiceResponse> getServicesByCategory(String category);
    
    ServiceResponse createService(CreateServiceRequest request);
    ServiceResponse getServiceById(Long id);
    List<ServiceResponse> getAllServices();

    List<ServiceResponse> getServicesByProvider(Long providerId);

    List<ServiceResponse> getServicesByProviderAndCategory(Long providerId, String category);

    List<ServiceResponse> getMyServices();
    ServiceResponse updateService(Long serviceId ,  CreateServiceRequest request);
    void deleteService(Long serviceId);
}