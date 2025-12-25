package com.example.demo.security;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {

    @Override
    public String generateToken(String email, String role, Long userId) {
        return "mock-jwt-token-" + userId + "-" + role;
    }

    @Override
    public boolean validateToken(String token) {
        return token != null && token.startsWith("mock-jwt-token-");
    }

    @Override
    public Long getUserIdFromToken(String token) {
        if (token == null) return null;
        String[] parts = token.split("-");
        return parts.length > 2 ? Long.parseLong(parts[2]) : null;
    }

    @Override
    public String getRoleFromToken(String token) {
        if (token == null) return null;
        String[] parts = token.split("-");
        return parts.length > 3 ? parts[3] : null;
    }
}
