package com.user_service.user_service.controller;

import com.user_service.user_service.dto.RegisterRequestDTO;
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

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequestDTO dto) {

        return ResponseEntity.status(201).body(service.register(dto));
    }

    @GetMapping
    public String check() {
        return "Its working";
    }
}
