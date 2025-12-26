package com.onlinefood.menu_service.controller;

import com.onlinefood.menu_service.dto.MenuCreateRequestDTO;
import com.onlinefood.menu_service.dto.MenuUpdateRequestDTO;
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

    // ================= PROVIDER =================

    @PostMapping
    public Object createMenu(
            @PathVariable String providerId,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @RequestHeader(value = "X-ROLE", required = false) String role,
            @Valid @RequestBody MenuCreateRequestDTO dto) {

        authorizeProvider(providerId, userId, role);
        return service.createMenu(providerId, dto);
    }

    @GetMapping
    public Object getProviderMenus(
            @PathVariable String providerId,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role) {

        authorizeProvider(providerId, userId, role);
        return service.getMenusByProvider(providerId);
    }

    @PutMapping("/{menuId}")
    public Object updateMenu(
            @PathVariable String providerId,
            @PathVariable String menuId,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role,
            @Valid @RequestBody MenuUpdateRequestDTO dto) {

        authorizeProvider(providerId, userId, role);
        return service.updateMenu(menuId, providerId, dto);
    }

    @PatchMapping("/{menuId}/status")
    public Object toggleMenu(
            @PathVariable String providerId,
            @PathVariable String menuId,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role,
            @RequestParam boolean active) {

        authorizeProvider(providerId, userId, role);
        return service.updateMenuStatus(menuId, providerId, active);
    }

    // ================= PUBLIC =================

    @GetMapping("/public")
    public Object getPublicMenus(@PathVariable String providerId) {
        return service.getActiveMenusByProvider(providerId);
    }

    // ================= COMMON =================

    private void authorizeProvider(String providerId, String userId, String role) {
        if (userId == null || role == null) {
            throw new MenuException("Unauthorized access (gateway only)");
        }
        if (!"PROVIDER".equalsIgnoreCase(role)) {
            throw new MenuException("Only PROVIDER allowed");
        }
        if (!providerId.equals(userId)) {
            throw new MenuException("Unauthorized provider access");
        }
    }
}
