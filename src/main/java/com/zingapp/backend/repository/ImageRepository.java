package com.zingapp.backend.repository;

import com.zingapp.backend.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findFirstByProfileIdAndImageType(Long profileId, String imageType);
}

