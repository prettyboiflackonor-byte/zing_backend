package com.zingapp.backend.controller;

import com.zingapp.backend.model.User;
import com.zingapp.backend.dto.UserDTO;
import com.zingapp.backend.repository.UserRepository;
import com.zingapp.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "https://zing-frontend-phi.vercel.app/")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User newUser = userService.registerUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Login-endepunkt
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent() &&
                passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {

            User u = existingUser.get();
            UserDTO dto = new UserDTO(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getGender());
            return new ResponseEntity<>(dto, HttpStatus.OK);

        } else {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String query) {
        List<User> allUsers = userRepository.findAll();

        List<UserDTO> results = allUsers.stream()
                .filter(u -> u.getFirstName().toLowerCase().startsWith(query.toLowerCase()))
                .limit(5)
                .map(u -> new UserDTO(u.getId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getGender()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }
}
