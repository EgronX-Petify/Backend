package com.example.petify.domain.product.repository;

import com.example.petify.domain.product.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findByName(String tagName);
}
