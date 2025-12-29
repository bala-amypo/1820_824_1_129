package com.example.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // 256-bit key (Base64 encoded)
    private static final String SECRET_KEY_BASE64 =
            "c3ByaW5nLWJvb3Qtand0LXNlY3JldC1rZXktMjU2";

    private static final long VALIDITY_MS = 60 * 60 * 1000; // 1 hour

    private final SecretKey secretKey;

    public JwtTokenProvider() {
        this.secretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(SECRET_KEY_BASE64)
        );
    }

    public String generateToken(String email, String role, Long userId) {

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        claims.put("userId", userId);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + VALIDITY_MS);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
