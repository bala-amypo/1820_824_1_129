package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // if you are calling APIs from Postman/React without CSRF token, keep it disabled
            .csrf(AbstractHttpConfigurer::disable)

            // allow everything without authentication
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())

            // disable the default generated login page
            .formLogin(AbstractHttpConfigurer::disable)

            // disable basic auth popup also (optional)
            .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
