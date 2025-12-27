package com.example.demo.security;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    
    public String generateToken(String email, String role, Long userId) {
        // Simple token generation for testing purposes
        return "jwt-token-" + email + "-" + role + "-" + userId;
    }
    
    public boolean validateToken(String token) {
        return token != null && token.startsWith("jwt-token-");
    }
}