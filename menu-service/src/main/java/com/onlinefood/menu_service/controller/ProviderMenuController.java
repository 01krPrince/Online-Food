package com.onlinefood.menu_service.controller;

import com.onlinefood.menu_service.dto.ProviderMenuRequestDTO;
import com.onlinefood.menu_service.exception.MenuException;
import com.onlinefood.menu_service.service.ProviderMenuService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/providers/{providerId}/menu")
public class ProviderMenuController {

    private final ProviderMenuService service;

    public ProviderMenuController(ProviderMenuService service) {
        this.service = service;
    }

    // PROVIDER ONLY
    @PostMapping
    public Object addOrUpdateMenu(
            @PathVariable String providerId,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @RequestHeader(value = "X-ROLE", required = false) String role,
            @Valid @RequestBody ProviderMenuRequestDTO dto) {

        if (userId == null || role == null) {
            throw new MenuException("Gateway authorization required");
        }

        if (!"PROVIDER".equalsIgnoreCase(role)) {
            throw new MenuException("Only PROVIDER can manage menu");
        }

        if (!providerId.equals(userId)) {
            throw new MenuException("Unauthorized provider access");
        }

        return service.addOrUpdateMenu(providerId, dto);
    }


    // PROVIDER: VIEW OWN MENU
    @GetMapping
    public Object getProviderMenu(
            @PathVariable String providerId,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role) {

        if (!"PROVIDER".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only PROVIDER can view menu");
        }

        if (!providerId.equals(userId)) {
            throw new RuntimeException("Unauthorized provider access");
        }

        return service.getProviderMenu(providerId);
    }
}
