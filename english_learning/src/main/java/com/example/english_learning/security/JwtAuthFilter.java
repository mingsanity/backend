package com.example.english_learning.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            JwtService.JwtPayload payload = jwtService.verify(token);

            String role = (payload.role == null || payload.role.isBlank()) ? "USER" : payload.role.trim();
            String up = role.toUpperCase();

            // ✅ accept ADMIN or ROLE_ADMIN safely
            String authority = up.startsWith("ROLE_") ? up : "ROLE_" + up;

            var auth = new UsernamePasswordAuthenticationToken(
                    payload,
                    null,
                    List.of(new SimpleGrantedAuthority(authority))
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            chain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(401);
        }
    }
}
