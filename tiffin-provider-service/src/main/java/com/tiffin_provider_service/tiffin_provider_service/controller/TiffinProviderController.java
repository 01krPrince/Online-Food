package com.tiffin_provider_service.tiffin_provider_service.controller;

import com.tiffin_provider_service.tiffin_provider_service.dto.TiffinProviderRequestDTO;
import com.tiffin_provider_service.tiffin_provider_service.dto.TiffinProviderResponseDTO;
import com.tiffin_provider_service.tiffin_provider_service.exception.ProviderException;
import com.tiffin_provider_service.tiffin_provider_service.exception.UnauthorizedException;
import com.tiffin_provider_service.tiffin_provider_service.service.TiffinProviderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/providers")
public class TiffinProviderController {

    private final TiffinProviderService service;

    @Value("${GATEWAY_INTERNAL_SECRET}")
    private String gatewaySecret;

    public TiffinProviderController(TiffinProviderService service) {
        this.service = service;
    }

    // ================= CUSTOMER =================

    /**
     * CUSTOMER applies to become PROVIDER
     */
//    @PostMapping("/apply")
//    public ResponseEntity<TiffinProviderResponseDTO> apply(
//            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
//            @RequestHeader(value = "X-USER-ID", required = false) String userId,
//            @RequestHeader(value = "X-ROLE", required = false) String role,
//            @Valid @RequestBody TiffinProviderRequestDTO dto) {
//
//        authorizeCustomer(userId, role, internalKey);
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(service.apply(userId, role, dto));
//    }

    @PostMapping("/apply")
    public ResponseEntity<TiffinProviderResponseDTO> apply(
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @RequestHeader(value = "X-ROLE", required = false) String role,
            @Valid @RequestBody TiffinProviderRequestDTO dto) {

        System.out.println("INTERNAL KEY FROM GATEWAY = " + internalKey);
        System.out.println("USER ID = " + userId);
        System.out.println("ROLE = " + role);
        System.out.println("EXPECTED INTERNAL KEY = " + gatewaySecret);

        authorizeCustomer(userId, role, internalKey);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.apply(userId, role, dto));
    }


    // ================= ADMIN =================

    /**
     * ADMIN approves / blocks provider
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestHeader(value = "X-ROLE", required = false) String role,
            @PathVariable String id,
            @RequestParam String status) {

        authorizeAdmin(role, internalKey);

        service.updateStatus(id, role, status);
        return ResponseEntity.ok("Provider status updated");
    }

    // ================= PUBLIC =================

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }

    // ================= COMMON =================

    private void authorizeCustomer(String userId, String role, String internalKey) {

        if (internalKey == null || !gatewaySecret.equals(internalKey)) {
            throw new UnauthorizedException("Invalid internal gateway key");
        }

        if (userId == null) {
            throw new UnauthorizedException("User not authenticated");
        }
    }


    private void authorizeAdmin(
            String role,
            String internalKey
    ) {
        if (internalKey == null || !internalKey.equals(gatewaySecret)) {
            throw new ProviderException("Direct access forbidden");
        }
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new ProviderException("Only ADMIN allowed");
        }
    }
}
