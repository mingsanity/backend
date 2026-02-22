package com.example.english_learning.service;

import com.example.english_learning.security.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthContext {

    private JwtService.JwtPayload payload() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof JwtService.JwtPayload p)) {
            throw new RuntimeException("Unauthorized");
        }
        return p;
    }

    public Long userId() { return payload().userId; }
    public String email() { return payload().email; }
    public String role() { return payload().role; }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(role());
    }
}
