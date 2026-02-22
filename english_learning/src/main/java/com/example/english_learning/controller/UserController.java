package com.example.english_learning.controller;

import com.example.english_learning.dto.UserMeResponse;
import com.example.english_learning.security.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public UserMeResponse me(Authentication authentication) {
        // JwtAuthFilter set principal = JwtPayload
        JwtService.JwtPayload payload = (JwtService.JwtPayload) authentication.getPrincipal();
        return new UserMeResponse(payload.userId, payload.email);
    }
}
