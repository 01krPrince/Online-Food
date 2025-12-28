package com.api_gateway.api_gateway.security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final String gatewaySecret;

    public JwtAuthenticationFilter(
            JwtUtil jwtUtil,
            @Value("${GATEWAY_INTERNAL_SECRET}") String gatewaySecret
    ) {
        this.jwtUtil = jwtUtil;
        this.gatewaySecret = gatewaySecret;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        String path = exchange.getRequest().getURI().getPath();

        // 🔓 Public endpoints
        if (path.contains("/users/login")
                || path.contains("/users/register")
                || path.contains("/users/verify-otp")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.validateAndGetClaims(token);

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            ServerWebExchange mutatedExchange =
                    exchange.mutate()
                            .request(exchange.getRequest().mutate()
                                    .headers(headers -> {
                                        headers.set("X-USER-ID", userId);
                                        headers.set("X-ROLE", role);
                                        headers.set("X-INTERNAL-KEY", gatewaySecret);
                                    })
                                    .build())
                            .build();

            return chain.filter(mutatedExchange);

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
