package com.example.springapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring App API")
                        .version("1.0.0")
                        .description("API documentation for Spring App - User and Account management system")
                        .contact(new Contact()
                                .name("API Support")
                                .email("support@example.com")))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("basicAuth", 
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springapp-public")
                .packagesToScan("com.example.springapp.controller")
                .build();
    }
}