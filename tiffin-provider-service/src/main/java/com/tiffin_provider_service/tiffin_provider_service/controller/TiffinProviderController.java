package com.tiffin_provider_service.tiffin_provider_service.controller;

import com.tiffin_provider_service.tiffin_provider_service.dto.TiffinProviderRequestDTO;
import com.tiffin_provider_service.tiffin_provider_service.dto.TiffinProviderResponseDTO;
import com.tiffin_provider_service.tiffin_provider_service.service.TiffinProviderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/providers")
public class TiffinProviderController {

    private final TiffinProviderService service;

    public TiffinProviderController(TiffinProviderService service) {
        this.service = service;
    }

    // PROVIDER APPLY (logged-in user only)
    @PostMapping("/apply")
    public ResponseEntity<TiffinProviderResponseDTO> apply(
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role,
            @Valid @RequestBody TiffinProviderRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.apply(userId, role, dto));
    }

    // ADMIN ONLY
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @RequestHeader("X-ROLE") String role,
            @PathVariable String id,
            @RequestParam String status) {

        service.updateStatus(id, role, status);
        return ResponseEntity.ok("Provider status updated");
    }

    // PUBLIC / ADMIN
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }
}
