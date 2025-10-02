package com.example.petify.controller.pet;

import com.example.petify.model.service.ServiceCategory;
import com.example.petify.dto.pet.ServiceResponse;
import com.example.petify.service.pet.AppointmentService;
import com.example.petify.service.pet.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ServiceController {

    private final ServiceService serviceService;
    private final AppointmentService appointmentService;


    @GetMapping("/service/search")
    public ResponseEntity<List<ServiceResponse>> searchServices(
            @RequestParam String searchTerm) {
        List<ServiceResponse> services = serviceService.searchServices(searchTerm);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/service/categories")
    public ResponseEntity<ServiceCategory[]> getServiceCategories() {
        return ResponseEntity.ok(ServiceCategory.values());
    }


    @GetMapping("/service")
    public ResponseEntity<List<ServiceResponse>> getAllServices(
            @RequestParam(required = false) String category ,
            @RequestParam(required = false) Long providerId) {

        List<ServiceResponse> services = null;

        if((category != null && !category.isEmpty()) && providerId != null){
            services = serviceService.getServicesByProviderAndCategory(providerId, category);
        }
        else if(category != null && !category.isEmpty()){
            services = serviceService.getServicesByCategory(category);
        }
        else if(providerId != null){
            services = serviceService.getServicesByProvider(providerId);
        }
        else{
            services = serviceService.getAllServices();
        }

        return ResponseEntity.ok(services);
    }


    @GetMapping("/service/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable Long id) {
        ServiceResponse service = serviceService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    

}