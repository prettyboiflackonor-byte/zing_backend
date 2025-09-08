package com.zingapp.backend.controller;

import com.zingapp.backend.dto.ProfileDTO;
import com.zingapp.backend.model.Profile;
import com.zingapp.backend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // Get all profiles
    @GetMapping
    public ResponseEntity<List<Profile>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    // Find user Profile by id (with profilePicUrl and bannerUrl)
    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getProfileById(@PathVariable Long id) {
        try {
            ProfileDTO dto = profileService.getProfileDto(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update bio
    @PutMapping("/{id}/bio")
    public ResponseEntity<Profile> updateBio(@PathVariable Long id, @RequestBody String newBio) {
        return ResponseEntity.ok(profileService.updateBio(id, newBio));
    }

    // Update users name on profile
    @PutMapping("/{id}/name")
    public ResponseEntity<?> updateName(@PathVariable Long id, @RequestBody Map<String, String> names) {
        String firstName = names.get("firstName");
        String lastName = names.get("lastName");
        profileService.updateName(id, firstName, lastName);
        return ResponseEntity.ok().build();
    }
}
