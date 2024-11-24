package com.finance.api_gateway.infrastructure.config;

import com.finance.api_gateway.infrastructure.fliter.AuthenticationFilter;
import com.finance.api_gateway.infrastructure.fliter.RouteValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FilterConfig {

//    @Bean
//    public RouteValidator routeValidator() {
//        return new RouteValidator();
//    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public AuthenticationFilter authenticationFilter(WebClient.Builder webClientBuilder,
                                                     RouteValidator routeValidator) {
        return new AuthenticationFilter(webClientBuilder, routeValidator);
    }
}
