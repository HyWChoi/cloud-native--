package com.finance.api_gateway.infrastructure.fliter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteValidator {
    public static final List<String> openApiEndpoints = List.of(
            "/user_service/profile/register",
            "/user_service/profile/login"
    );

    public boolean isSecured(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        for (String endpoint : openApiEndpoints) {
            if (path.equals(endpoint)) {
                return false;
            }
        }
        return true;
    }
}