package com.user_service.user_service.exception; // (Or your provider package)

public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
}