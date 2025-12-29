package com.example.demo.security;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private final String secret = "dummy-secret";
    private final long validityInMs = 3600000;

    public String generateToken(String email, String role, Long userId) {
        // Stub implementation – tests mock this
        return "jwt-token";
    }

    public boolean validateToken(String token) {
        return token != null && !token.isBlank();
    }
}
