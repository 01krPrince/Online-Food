package com.tiffin_provider_service.tiffin_provider_service.controller;

import com.tiffin_provider_service.tiffin_provider_service.dto.TiffinProviderRequestDTO;
import com.tiffin_provider_service.tiffin_provider_service.dto.TiffinProviderResponseDTO;
import com.tiffin_provider_service.tiffin_provider_service.service.TiffinProviderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/providers")
public class TiffinProviderController {

    @Autowired
    private TiffinProviderService service;

    @PostMapping("/register")
    public ResponseEntity<TiffinProviderResponseDTO> register(
            @Valid @RequestBody TiffinProviderRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.registerProvider(dto));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAllProviders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getProviderById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable String id,
            @RequestParam String status) {

        service.updateStatus(id, status);
        return ResponseEntity.ok("Status updated successfully");
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getAllByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.getAllByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProvider(
            @PathVariable String id,
            @RequestBody TiffinProviderRequestDTO dto) {

        return ResponseEntity.ok(service.updateProvider(id, dto));
    }
}
