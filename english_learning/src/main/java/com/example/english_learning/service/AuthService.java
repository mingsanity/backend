package com.example.english_learning.service;

import com.example.english_learning.dto.*;
import com.example.english_learning.model.User;
import com.example.english_learning.repository.UserRepository;
import com.example.english_learning.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository userRepo, PasswordEncoder encoder, JwtService jwt) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public void register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.email)) throw new RuntimeException("Email already exists");
        User u = new User();
        u.setEmail(req.email);
        u.setPassword(encoder.encode(req.password));
        u.setRole("USER"); // default
        userRepo.save(u);
    }

    public AuthResponse login(LoginRequest req) {
        User u = userRepo.findByEmail(req.email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!encoder.matches(req.password, u.getPassword()))
            throw new RuntimeException("Invalid credentials");

        // ✅ normalize role for JWT: always "ADMIN"/"USER" (never "ROLE_ADMIN")
        String role = (u.getRole() == null || u.getRole().isBlank()) ? "USER" : u.getRole().trim();
        if (role.toUpperCase().startsWith("ROLE_")) role = role.substring("ROLE_".length());

        return new AuthResponse(jwt.create(u.getId(), u.getEmail(), role));
    }
}
