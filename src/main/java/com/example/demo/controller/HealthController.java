package com.example.demo.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String health(HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.write("BUNDLE-OK");
        writer.flush();
        return "BUNDLE-OK";
    }
}
