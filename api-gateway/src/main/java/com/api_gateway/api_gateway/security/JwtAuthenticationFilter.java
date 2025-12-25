package com.api_gateway.api_gateway.security;

import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // 🔓 Public endpoints (NO JWT)
        if (path.contains("/users/login") ||
                path.contains("/users/register")) {
            return chain.filter(exchange);
        }

        String authHeader =
                exchange.getRequest().getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.validateAndGetClaims(token);

            String userId = claims.getSubject();        // sub
            String role = claims.get("role", String.class);

            // 🔒 OVERRIDE client headers (trust boundary)
            ServerWebExchange mutatedExchange =
                    exchange.mutate()
                            .request(
                                    exchange.getRequest().mutate()
                                            .headers(headers -> {
                                                headers.remove("X-USER-ID");
                                                headers.remove("X-ROLE");
                                                headers.add("X-USER-ID", userId);
                                                headers.add("X-ROLE", role);
                                            })
                                            .build()
                            )
                            .build();

            return chain.filter(mutatedExchange);

        } catch (Exception ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1; // highest priority
    }
}
