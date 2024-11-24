package com.finance.api_gateway.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    @Value("${service.user.url}")
    private String userServiceUrl;

    @Value("${service.transaction.url}")
    private String transactionServiceUrl;

    @Bean
    public RouteLocator CustomRouterLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/api/v1/profile/**")
                        .filters(f -> f
                                .rewritePath("/api/v1/profile/(?<segment>.*)",
                                        "/user_service/profile/${segment}")
                                .addResponseHeader("X-Service", "user-service"))
                        .uri(userServiceUrl))
                .route("transaction-service", r -> r
                        .path("/api/v1/transactions/**")
                        .filters(f -> f
                                .rewritePath("/api/v1/transactions/(?<segment>.*)",
                                        "/transaction_service/transactions/${segment}")
                                .addResponseHeader("X-Service", "transaction-service"))
                        .uri(transactionServiceUrl))
                .route("category-service", r -> r
                        .path("/api/v1/categories/**")
                        .filters(f -> f
                                .rewritePath("/api/v1/categories/(?<segment>.*)",
                                        "/transaction_service/categories/${segment}")
                                .addResponseHeader("X-Service", "transaction-service"))
                        .uri(transactionServiceUrl))
                .build();
    }
}
