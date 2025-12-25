package com.example.demo.security;

public interface JwtTokenProvider {
    String generateToken(String email, String role, Long userId);
    boolean validateToken(String token);
}
