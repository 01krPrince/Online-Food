package com.onlinefood.restaurant_service.controller;

import com.onlinefood.restaurant_service.dto.RestaurantRequestDTO;
import com.onlinefood.restaurant_service.dto.RestaurantResponseDTO;
import com.onlinefood.restaurant_service.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService service;

    @PostMapping("/register")
    public ResponseEntity<RestaurantResponseDTO> register(
            @Valid @RequestBody RestaurantRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.registerRestaurant(dto));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getRestaurantById(id));
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
    public ResponseEntity<?> updateRestaurant(
            @PathVariable String id,
            @RequestBody RestaurantRequestDTO dto) {

        return ResponseEntity.ok(service.updateRestaurant(id, dto));
    }



}
