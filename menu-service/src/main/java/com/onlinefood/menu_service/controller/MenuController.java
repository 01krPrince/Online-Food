package com.onlinefood.menu_service.controller;

import com.onlinefood.menu_service.dto.MenuCreateRequestDTO;
import com.onlinefood.menu_service.exception.MenuException;
import com.onlinefood.menu_service.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/providers/{providerId}/menus")
public class MenuController {

    private final MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    // PROVIDER: CREATE MENU
    @PostMapping
    public Object createMenu(
            @PathVariable String providerId,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @RequestHeader(value = "X-ROLE", required = false) String role,
            @Valid @RequestBody MenuCreateRequestDTO dto) {

        if (userId == null || role == null) {
            throw new MenuException("Unauthorized access (gateway only)");
        }

        if (!"PROVIDER".equalsIgnoreCase(role)) {
            throw new MenuException("Only PROVIDER can create menu");
        }

        if (!providerId.equals(userId)) {
            throw new MenuException("Unauthorized provider access");
        }

        return service.createMenu(providerId, dto);
    }

    // PROVIDER: GET OWN MENUS
    @GetMapping
    public Object getProviderMenus(
            @PathVariable String providerId,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role) {

        if (!"PROVIDER".equalsIgnoreCase(role)) {
            throw new MenuException("Only PROVIDER can view menus");
        }

        if (!providerId.equals(userId)) {
            throw new MenuException("Unauthorized provider access");
        }

        return service.getMenusByProvider(providerId);
    }

    // PUBLIC: VIEW PROVIDER MENUS
    @GetMapping("/public")
    public Object getPublicMenus(@PathVariable String providerId) {
        return service.getActiveMenusByProvider(providerId);
    }
}
