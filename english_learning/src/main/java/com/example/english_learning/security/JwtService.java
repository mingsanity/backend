package com.example.english_learning.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    public static class JwtPayload {
        public Long userId;
        public String email;
        public String role;

        public JwtPayload(Long userId, String email, String role) {
            this.userId = userId;
            this.email = email;
            this.role = role;
        }
    }

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String create(Long userId, String email, String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expirationMs))
                .signWith(key)
                .compact();
    }

    public JwtPayload verify(String token) {
        Jws<Claims> jws = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
        Claims c = jws.getPayload();

        Long userId = Long.valueOf(c.getSubject());
        String email = c.get("email", String.class);
        String role = c.get("role", String.class);

        return new JwtPayload(userId, email, role);
    }
}
