package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider("demo-secret", 3600000L);
    }
}
