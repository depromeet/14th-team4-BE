package com.depromeet.config;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        //
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://localhost:8080/");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://localhost:3000/");
        //
        //
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("DELETE");
        config.setAllowCredentials(true);
        //
        config.addAllowedHeader("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
