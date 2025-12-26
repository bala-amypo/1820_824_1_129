package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Server server = new Server();
        server.setUrl("https://9024.32procr.amypo.ai/");
        server.setDescription("Deployed Server");

        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce Bundle & Save API")
                        .version("1.0")
                        .description("Swagger documentation for Bundle & Save backend"))
                .servers(List.of(server));
    }
}
