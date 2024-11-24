package com.finance.api_gateway.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 허용할 origin 설정
        config.addAllowedOrigin("http://localhost");
        config.addAllowedOrigin("http://localhost:80");


        // HTTP 메서드 설정
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");

        // 헤더 설정
        config.addAllowedHeader("X-Session-ID");
        config.addAllowedHeader("X-User-ID");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("X-Requested-With");

        // 응답 헤더 노출 설정
        config.addExposedHeader("X-Service");

        // 세션 기반 인증을 위한 설정
        config.addExposedHeader("Authorization"); // Authorization 헤더 노출

        // 인증 관련 설정
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 실제 서비스 경로에 맞게 CORS 설정
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}