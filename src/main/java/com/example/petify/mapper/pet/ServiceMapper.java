package com.example.petify.mapper.pet;

import com.example.petify.model.service.Services;
import com.example.petify.dto.user.serviceprovider.CreateServiceRequest;
import com.example.petify.dto.pet.ServiceResponse;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    public ServiceResponse mapToResponse(Services service) {
        return ServiceResponse.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .category(service.getCategory())
                .price(service.getPrice())
                .notes(service.getNotes())
                .providerName(service.getProvider().getName())
                .providerId(service.getProvider().getId())
                .build();
    }

    public void updateServiceFromRequest(Services service, CreateServiceRequest request) {
        if (request.getName() != null) {
            service.setName(request.getName());
        }
        if (request.getDescription() != null) {
            service.setDescription(request.getDescription());
        }
        if (request.getCategory() != null) {
            service.setCategory(request.getCategory());
        }
        if (request.getPrice() >= 0) {
            service.setPrice(request.getPrice());
        }
        if (request.getNotes() != null) {
            service.setNotes(request.getNotes());
        }
    }
}