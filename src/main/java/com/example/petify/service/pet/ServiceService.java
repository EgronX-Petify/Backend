package com.example.petify.service.pet;

import com.example.petify.dto.pet.CreateServiceRequest;
import com.example.petify.dto.pet.ServiceResponse;

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