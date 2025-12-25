package com.example.demo.dto;

public class LoginRequestDto {
    private String email;
    private String role;
    private Long userId;
    
    // Constructors, Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
