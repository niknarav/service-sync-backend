package com.example.servicesyncservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Разрешить все эндпоинты
                .allowedOrigins("http://localhost:3000/**") // Разрешить запросы с React
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Разрешенные методы
                .allowedHeaders("Authorization", "Content-Type") // Разрешить все заголовки
                .allowCredentials(true); // Разрешить передачу куки и авторизационных данных
    }
}
