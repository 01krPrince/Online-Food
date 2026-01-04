package com.user_service.user_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ========================================================================
    // 1. VALIDATION ERRORS (@Valid failed) - Returns 400
    // ========================================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(err -> 
            errors.put(err.getField(), err.getDefaultMessage())
        );

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Failed", errors);
    }

    // ========================================================================
    // 2. UNAUTHORIZED (Invalid Token / Gateway Key) - Returns 401
    // ========================================================================
    // Ensure you have this class, or change to standard AuthenticationException
    @ExceptionHandler(UnauthorizedException.class) 
    public ResponseEntity<Map<String, Object>> handleUnauthorized(UnauthorizedException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized Access", ex.getMessage());
    }

    // ========================================================================
    // 3. BUSINESS LOGIC (UserException, ProviderException, etc.) - Returns 400
    // ========================================================================
    // This catches UserException, ProviderException, IllegalArgumentException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleLogicException(RuntimeException ex) {
        // You can check 'ex' type here if you need 403 vs 400 differentiation
        return buildResponse(HttpStatus.BAD_REQUEST, "Operation Failed", ex.getMessage());
    }

    // ========================================================================
    // 4. FALLBACK (Code Crash / NullPointer) - Returns 500
    // ========================================================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        ex.printStackTrace(); // Helpful for logs
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
    }

    // ========================================================================
    // HELPER METHOD (Keeps code clean)
    // ========================================================================
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, Object details) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", error);
        
        // If details is a Map (validation errors), put it under 'errors'
        // If details is a String (message), put it under 'message'
        if (details instanceof Map) {
            response.put("validationErrors", details);
        } else {
            response.put("message", details);
        }

        return ResponseEntity.status(status).body(response);
    }
}