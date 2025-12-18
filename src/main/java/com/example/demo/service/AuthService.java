package com.example.demo.service;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;

public interface AuthService {

    void register(AuthRequest request);

    AuthResponse login(AuthRequest request);
}
