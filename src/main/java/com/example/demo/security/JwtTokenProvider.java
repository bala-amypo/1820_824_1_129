package com.example.demo.security;

public interface JwtTokenProvider {
    
    /**
     * Generates a JWT token with user claims
     * @param email User's email
     * @param role User's role (ADMIN, CUSTOMER)
     * @param userId User's ID
     * @return JWT token string
     */
    String generateToken(String email, String role, Long userId);
    
    /**
     * Validates a JWT token
     * @param token JWT token string
     * @return true if valid, false otherwise
     */
    boolean validateToken(String token);
    
    /**
     * Extracts userId from token (for security context)
     * @param token JWT token string
     * @return userId as Long
     */
    Long getUserIdFromToken(String token);
    
    /**
     * Extracts role from token
     * @param token JWT token string
     * @return role as String
     */
    String getRoleFromToken(String token);
}
