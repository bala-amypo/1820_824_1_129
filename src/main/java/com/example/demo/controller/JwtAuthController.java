package com.example.demo.controller;

import com.example.demo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class JwtAuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestParam String email,
            @RequestParam String role,
            @RequestParam Long userId) {
        String token = jwtTokenProvider.generateToken(email, role, userId);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValid = jwtTokenProvider.validateToken(token);
        return ResponseEntity.ok(isValid);
    }
}
