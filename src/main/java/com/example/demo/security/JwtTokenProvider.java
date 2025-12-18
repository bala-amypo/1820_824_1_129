package com.example.demo.security;

import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtTokenProvider {

    public String generateToken(String email) {
        // simple fake token (Base64)
        return Base64.getEncoder().encodeToString(email.getBytes());
    }

    public String getEmailFromToken(String token) {
        return new String(Base64.getDecoder().decode(token));
    }

    public boolean validateToken(String token) {
        try {
            Base64.getDecoder().decode(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
