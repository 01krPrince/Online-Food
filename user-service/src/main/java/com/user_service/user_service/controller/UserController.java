package com.user_service.user_service.controller;

import com.user_service.user_service.dto.LoginRequestDTO;
import com.user_service.user_service.dto.LoginResponseDTO;
import com.user_service.user_service.dto.RegisterRequestDTO;
import com.user_service.user_service.enums.Role;
import com.user_service.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    // ---------------- AUTH ----------------

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.status(201).body(service.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(service.login(dto));
    }

    // ---------------- PROFILE ----------------

    // USER / PROVIDER / ADMIN
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(
            @RequestHeader("X-USER-ID") String userId) {

        return ResponseEntity.ok(service.getUserById(userId));
    }

    // ---------------- ADMIN ONLY ----------------

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestHeader("X-ROLE") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only ADMIN can view users");
        }
        return ResponseEntity.ok(service.getAllUsers());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable String id,
            @RequestParam String status,
            @RequestHeader("X-ROLE") String role) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only ADMIN can update user status");
        }

        service.updateStatus(id, status);
        return ResponseEntity.ok("User status updated");
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<?> updateUserRole(
            @PathVariable String id,
            @RequestParam Role role,
            @RequestHeader("X-ROLE") String adminRole) {

        if (!"ADMIN".equalsIgnoreCase(adminRole)) {
            throw new RuntimeException("Only ADMIN can update role");
        }

        service.updateRole(id, role);
        return ResponseEntity.ok("User role updated");
    }


}
