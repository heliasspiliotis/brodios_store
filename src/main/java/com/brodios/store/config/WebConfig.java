package com.brodios.store.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Επιτρέπουμε τα πάντα
                .allowedOrigins("http://localhost:4200") // Μόνο από το Angular μας
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Επιτρεπόμενες ενέργειες
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
