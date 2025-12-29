package com.example.demo.security;

public class JwtTokenProvider {

    private final String secret;
    private final long validityInMs;

    public JwtTokenProvider(String secret, long validityInMs) {
        this.secret = secret;
        this.validityInMs = validityInMs;
    }

    public String generateToken(String email, String role, Long userId) {
        return "jwt-token";
    }

    public boolean validateToken(String token) {
        return token != null && !token.isBlank();
    }
}
