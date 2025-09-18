package com.example.petify.domain.service.repository;

import com.example.petify.domain.profile.model.Profile;
import com.example.petify.domain.profile.model.SPProfile;
import com.example.petify.domain.service.model.Services;
import com.example.petify.domain.service.model.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long> {

    @Query("SELECT s FROM Services s WHERE " +
           "LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Services> searchServices(@Param("searchTerm") String searchTerm);

    List<Services> findByCategory(ServiceCategory category);

    List<Services> findByProvider(Profile provider);

    List<Services> findByProviderAndCategory(SPProfile provider, ServiceCategory cat);
}