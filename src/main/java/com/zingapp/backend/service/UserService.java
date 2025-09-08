package com.zingapp.backend.service;

import com.zingapp.backend.model.User;
import com.zingapp.backend.model.Profile;
import com.zingapp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(User user) {
        // Sjekk om e-Post allerede finnes
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("E-mail already in use");
        } else {
            Profile profile = new Profile();
            profile.setUser(user);
            profile.setBio("");
            profile.setProfileImage(null);
            profile.setBannerImage(null);

            String hashedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashedPassword);

            // Lagrer profil til bruker
            user.setProfile(profile);

            // Lagre ny bruker i databasen
            return userRepository.save(user);
        }

    }
}