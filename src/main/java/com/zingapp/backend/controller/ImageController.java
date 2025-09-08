package com.zingapp.backend.controller;

import com.zingapp.backend.model.Image;
import com.zingapp.backend.model.Profile;
import com.zingapp.backend.repository.ImageRepository;
import com.zingapp.backend.repository.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/images")
@CrossOrigin(origins = "*")
public class ImageController {

    private final ImageRepository imageRepository;
    private final ProfileRepository profileRepository;
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    public ImageController(ImageRepository imageRepository, ProfileRepository profileRepository) {
        this.imageRepository = imageRepository;
        this.profileRepository = profileRepository;
    }

    private Path getUploadPath() {
        // Sørger for at vi lagrer i zingapp/uploads
        return Paths.get("../uploads");
    }

    private String saveFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadDir = getUploadPath();
        Files.createDirectories(uploadDir);
        Path filepath = uploadDir.resolve(filename);
        Files.write(filepath, file.getBytes());
        return filename;
    }

    @PostMapping("/upload")
    public ResponseEntity<Image> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("profileId") Long profileId) {
        try {
            String filename = saveFile(file);

            Image image = new Image();
            image.setUrl("/uploads/" + filename);
            image.setUploadedAt(LocalDateTime.now());
            image.setImageType("POST");

            Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new RuntimeException("Profile not found"));
            image.setProfile(profile);

            Image saved = imageRepository.save(image);
            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/uploadProfileImage")
    public ResponseEntity<Image> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("profileId") Long profileId) {
        try {
            String filename = saveFile(file);

            Image newImage = new Image();
            newImage.setUrl("/uploads/" + filename);
            newImage.setUploadedAt(LocalDateTime.now());
            newImage.setImageType("PROFILE_PIC");
            Image savedImage = imageRepository.save(newImage);

            Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new RuntimeException("Profile not found"));

            // Fjern gammelt profilbilde (unngå constraint error)
            Optional<Image> existingProfileImage = imageRepository.findFirstByProfileIdAndImageType(profileId, "PROFILE_PIC");
            existingProfileImage.ifPresent(oldImage -> {
                profile.setProfileImage(null);
                profileRepository.save(profile);
                imageRepository.delete(oldImage);
            });

            savedImage.setProfile(profile);
            imageRepository.save(savedImage);

            profile.setProfileImage(savedImage);
            profileRepository.save(profile);

            return ResponseEntity.ok(savedImage);

        } catch (IOException e) {
            logger.error("Failed to upload image", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/uploadBannerImage")
    public ResponseEntity<Image> uploadBannerImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("profileId") Long profileId) {
        try {
            String filename = saveFile(file);

            Image newImage = new Image();
            newImage.setUrl("/uploads/" + filename);
            newImage.setUploadedAt(LocalDateTime.now());
            newImage.setImageType("BANNER");
            Image savedImage = imageRepository.save(newImage);

            Profile profile = profileRepository.findById(profileId)
                    .orElseThrow(() -> new RuntimeException("Profile not found"));

            // Fjern gammelt bannerbilde
            Optional<Image> existingBannerImage = imageRepository.findFirstByProfileIdAndImageType(profileId, "BANNER");
            existingBannerImage.ifPresent(oldImage -> {
                profile.setBannerImage(null);
                profileRepository.save(profile);
                imageRepository.delete(oldImage);
            });

            savedImage.setProfile(profile);
            imageRepository.save(savedImage);

            profile.setBannerImage(savedImage);
            profileRepository.save(profile);

            return ResponseEntity.ok(savedImage);

        } catch (IOException e) {
            logger.error("Failed to upload image", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
