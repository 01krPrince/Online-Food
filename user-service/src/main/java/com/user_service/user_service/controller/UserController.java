package com.user_service.user_service.controller;

import com.user_service.user_service.dto.customer.LoginRequestDTO;
import com.user_service.user_service.dto.customer.LoginResponseDTO;
import com.user_service.user_service.dto.customer.RegisterRequestDTO;
import com.user_service.user_service.dto.VerifyOtpDTO;
import com.user_service.user_service.enums.Role;
import com.user_service.user_service.exception.UserException;
import com.user_service.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @Value("${GATEWAY_INTERNAL_SECRET}")
    private String gatewaySecret;

    public UserController(UserService service) {
        this.service = service;
    }

    // ---------------- AUTH (PUBLIC) ----------------

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.status(201).body(service.register(dto));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpDTO dto) {
        service.verifyOtp(dto);
        return ResponseEntity.ok("Account verified successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(service.login(dto));
    }

    // ---------------- PROFILE ----------------

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestHeader(value = "X-USER-ID", required = false) String userId) {

        authorizeLoggedInUser(userId, internalKey);
        return ResponseEntity.ok(service.getUserById(userId));
    }

    // ---------------- ADMIN ONLY ----------------

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestHeader(value = "X-ROLE", required = false) String role) {

        authorizeAdmin(role, internalKey);
        return ResponseEntity.ok(service.getAllUsers());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable String id,
            @RequestParam String status,
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestHeader(value = "X-ROLE", required = false) String role) {

        authorizeAdmin(role, internalKey);
        service.updateStatus(id, status);
        return ResponseEntity.ok("User status updated");
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<?> updateUserRole(
            @PathVariable String id,
            @RequestParam Role role,
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestHeader(value = "X-ROLE", required = false) String adminRole) {

        authorizeAdmin(adminRole, internalKey);
        service.updateRole(id, role);
        return ResponseEntity.ok("User role updated");
    }

    // ---------------- COMMON ----------------

    private void authorizeLoggedInUser(
            String userId,
            String internalKey
    ) {
        if (internalKey == null || !internalKey.equals(gatewaySecret)) {
            throw new UserException("Direct access forbidden");
        }
        if (userId == null) {
            throw new UserException("Unauthorized access (gateway only)");
        }
    }

    private void authorizeAdmin(
            String role,
            String internalKey
    ) {
        if (internalKey == null || !internalKey.equals(gatewaySecret)) {
            throw new UserException("Direct access forbidden");
        }
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new UserException("Only ADMIN allowed");
        }
    }
}
