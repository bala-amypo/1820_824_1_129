package com.example.demo.service;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest req);

    void login(AuthRequest req);
}
