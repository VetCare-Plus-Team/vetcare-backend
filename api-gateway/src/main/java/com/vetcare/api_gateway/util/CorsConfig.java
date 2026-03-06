package com.vetcare.api_gateway.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // React frontend eke URL eka methanata denawa (3000 wela run unoth ekath danna
        // puluwan)
        // corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:5174",
        // "http://localhost:5173"));
        corsConfig.setAllowedOriginPatterns(Arrays.asList("*"));
        corsConfig.setMaxAge(3600L); // Pre-flight request cache time eka

        // Frontend eken karanna puluwan actions (GET, POST, etc.)
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Headers allow kirima (Auth tokens yawanna meka awashyai)
        corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-User-Role"));
        corsConfig.setAllowCredentials(true); // Token ekka yaddi meka true wenna one

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Hama API endpoint ekakatama meka apply karanawa

        return new CorsWebFilter(source);
    }
}
