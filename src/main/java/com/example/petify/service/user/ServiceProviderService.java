package com.example.petify.service.user;

import com.example.petify.dto.user.serviceprovider.CreateServiceRequest;
import com.example.petify.dto.pet.ServiceResponse;
import com.example.petify.dto.user.serviceprovider.ServiceProviderResponse;

import java.util.List;

public interface ServiceProviderService {
    List<ServiceResponse> getMyServices();
    ServiceResponse updateService(Long serviceId ,  CreateServiceRequest request);
    void deleteService(Long serviceId);
    ServiceResponse createService(CreateServiceRequest request);
    List<ServiceProviderResponse> getAllServiceProviders();
    List<ServiceResponse> getServicesByProvider(Long providerId);
}
