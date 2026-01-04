package com.user_service.user_service.controller;

import com.user_service.user_service.dto.provider.ProviderRegistrationDTO;
import com.user_service.user_service.dto.provider.TiffinProviderResponseDTO;
import com.user_service.user_service.exception.UnauthorizedException;
import com.user_service.user_service.service.TiffinProviderService;
import com.user_service.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.ProviderException;
import java.util.Map;

@RestController
@RequestMapping("/providers")
public class TiffinProviderController {

    private final TiffinProviderService providerService;
    private final UserService userService;

    @Value("${GATEWAY_INTERNAL_SECRET}")
    private String gatewaySecret;

    public TiffinProviderController(TiffinProviderService providerService, UserService userService) {
        this.providerService = providerService;
        this.userService = userService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestBody Map<String, String> payload) {

        validateGateway(internalKey);

        String email = payload.get("email");
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        userService.sendOtp(email);
        return ResponseEntity.ok("OTP sent successfully");
    }

    // ================= NEW REGISTRATION FLOW (Frontend uses this) =================

    @PostMapping("/register")
    public ResponseEntity<TiffinProviderResponseDTO> registerNewProvider(
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @Valid @RequestBody ProviderRegistrationDTO dto) {

        validateGateway(internalKey);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(providerService.registerNewProvider(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestHeader(value = "X-USER-ID", required = false) String userId) {

        validateGateway(internalKey);

        if (userId == null || userId.isEmpty()) {
            throw new UnauthorizedException("User ID is missing from headers");
        }

        return ResponseEntity.ok(providerService.getByUserId(userId));
    }

    // ================= ADMIN & PUBLIC =================

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestHeader(value = "X-ROLE", required = false) String role,
            @PathVariable String id,
            @RequestParam String status) {

        authorizeAdmin(role, internalKey);
        providerService.updateStatus(id, status);
        return ResponseEntity.ok("Provider status updated");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @PathVariable String id) {

        validateGateway(internalKey);
        return ResponseEntity.ok(providerService.getById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getByStatus(
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @PathVariable String status) {

        validateGateway(internalKey);
        return ResponseEntity.ok(providerService.getByStatus(status));
    }

    // ================= SECURITY CHECKS =================

    private void validateGateway(String internalKey) {
        if (internalKey == null || !internalKey.equals(gatewaySecret)) {
            throw new UnauthorizedException("Direct access forbidden: Invalid Gateway Key");
        }
    }

    private void authorizeAdmin(String role, String internalKey) {
        validateGateway(internalKey);
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ProviderException("Access Denied: Only ADMIN allowed");
        }
    }
}