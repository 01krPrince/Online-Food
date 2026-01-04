package com.api_gateway.api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
public class JwtUtil {

    private final Key key;

    // Constructor Logs
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        System.out.println("---- JWT UTIL INIT START ----");

        if (secret == null) {
            System.err.println("!!!! CRITICAL: JWT SECRET IS NULL !!!!");
            throw new IllegalStateException("JWT_SECRET is null");
        }

        System.out.println("Secret Key Length: " + secret.length());

        if (secret.length() < 32) {
            System.err.println("!!!! CRITICAL: SECRET KEY TOO SHORT (<32 chars) !!!!");
            throw new IllegalStateException("JWT_SECRET too short");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        System.out.println("---- JWT UTIL INIT SUCCESS ----");
    }

    public Claims validateAndGetClaims(String token) {
        System.out.println("JwtUtil: Checking Token Signature...");
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("JwtUtil: Token Valid! User ID: " + claims.getSubject());
            System.out.println("JwtUtil: User Role: " + claims.get("role"));
            return claims;

        } catch (io.jsonwebtoken.security.SignatureException e) {
            System.err.println("!!!! JWT ERROR: Signature Mismatch (Wrong Secret Key) !!!!");
            System.err.println("Gateway Key vs User-Service Key alag alag hai.");
            throw e;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.err.println("!!!! JWT ERROR: Token Expired !!!!");
            throw e;
        } catch (Exception e) {
            System.err.println("!!!! JWT ERROR: " + e.getMessage());
            e.printStackTrace(); // Full detail ke liye
            throw e;
        }
    }
}