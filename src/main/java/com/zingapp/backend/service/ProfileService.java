package com.zingapp.backend.service;

import com.zingapp.backend.dto.ProfileDTO;
import com.zingapp.backend.model.Image;
import com.zingapp.backend.model.Profile;
import com.zingapp.backend.model.User;
import com.zingapp.backend.repository.ImageRepository;
import com.zingapp.backend.repository.ProfileRepository;
import com.zingapp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository; // <-- Ny injeksjon

    @Autowired
    public ProfileService(ProfileRepository profileRepository,
                          ImageRepository imageRepository,
                          UserRepository userRepository) { // <-- Oppdatert konstruktør
        this.profileRepository = profileRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public ProfileDTO getProfileDto(Long profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        ProfileDTO dto = new ProfileDTO();
        dto.setId(profile.getId());
        dto.setBio(profile.getBio());

        // Hent User direkte fra Profile
        if (profile.getUser() != null) {
            dto.setFirstName(profile.getUser().getFirstName());
            dto.setLastName(profile.getUser().getLastName());
            dto.setEmail(profile.getUser().getEmail());
        }

        Optional<Image> profilePic = imageRepository.findFirstByProfileIdAndImageType(profileId, "PROFILE_PIC");
        Optional<Image> bannerPic = imageRepository.findFirstByProfileIdAndImageType(profileId, "BANNER");

        dto.setProfilePicUrl(profilePic.map(Image::getUrl).orElse(null));
        dto.setBannerUrl(bannerPic.map(Image::getUrl).orElse(null));

        return dto;
    }

    public Profile updateBio(Long id, String newBio) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        if (newBio.length() > 250) {
            throw new IllegalArgumentException("Bio too long");
        }

        profile.setBio(newBio);
        return profileRepository.save(profile);
    }

    public Profile updateBanner(Long id, Long imageId) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        profile.setBannerImage(image);
        return profileRepository.save(profile);
    }

    public void updateName(Long id, String firstName, String lastName) {
        Profile profile = profileRepository.findById(id).orElseThrow();
        User user = profile.getUser();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        userRepository.save(user);
    }
}