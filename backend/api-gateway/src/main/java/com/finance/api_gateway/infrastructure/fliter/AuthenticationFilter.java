package com.finance.api_gateway.infrastructure.fliter;

import com.finance.api_gateway.infrastructure.dto.ProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticationFilter implements GlobalFilter {
    private final WebClient.Builder webClientBuilder;
    private final RouteValidator routeValidator;

    @Value("${service.user.url}")
    private String userServiceUrl;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routeValidator.isSecured(request)) {
            String sessionId = getSessionId(request);

            if (sessionId == null) {
                return onError(exchange, "Missing session ID", HttpStatus.UNAUTHORIZED);
            }

            return webClientBuilder.build()
                    .get()
                    .uri(userServiceUrl + "/user_service/profile/me")
                    .header("X-Session-ID", sessionId)
                    .retrieve()
                    .bodyToMono(ProfileResponse.class)
                    .flatMap(profile -> {
                        // Long을 String으로 변환
                        String userId = String.valueOf(profile.getId());
                        log.debug("User ID from profile: {}", userId);  // 로깅 추가

                        ServerHttpRequest modifiedRequest = request.mutate()
                                .headers(headers -> {
                                    headers.remove("X-Session-ID");
                                    headers.add("X-USER-ID", userId);
                                })
                                .build();


                        // 헤더 확인을 위한 로깅
                        modifiedRequest.getHeaders().forEach((key, value) ->
                                log.debug("Header {}: {}", key, value));

                        return chain.filter(exchange.mutate()
                                .request(modifiedRequest)
                                .build());
                    })
                    .onErrorResume(WebClientResponseException.class, error -> {
                        HttpStatus status = (HttpStatus) error.getStatusCode();
                        log.error("Profile validation failed: {}", error.getMessage());
                        return onError(exchange, "Authentication failed", status);
                    })
                    .onErrorResume(Exception.class, error -> {
                        log.error("Unexpected error during authentication: {}", error.getMessage());
                        return onError(exchange, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
                    });
        }

        return chain.filter(exchange);
    }

    private String getSessionId(ServerHttpRequest request) {
        List<String> sessionHeader = request.getHeaders().get("X-Session-ID");
        return sessionHeader != null && !sessionHeader.isEmpty() ?
                sessionHeader.get(0).replace("[", "").replace("]", "") : null;  // 대괄호 제거
    }

    private Mono<Void> onError(ServerWebExchange exchange,
                               String message,
                               HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);

        String errorBody = "{\"error\": \"" + message + "\"}";
        DataBuffer buffer = response.bufferFactory()
                .wrap(errorBody.getBytes());

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.just(buffer));
    }
}