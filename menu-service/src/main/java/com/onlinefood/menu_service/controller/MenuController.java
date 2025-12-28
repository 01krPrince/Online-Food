package com.onlinefood.menu_service.controller;

import com.onlinefood.menu_service.dto.MenuCreateRequestDTO;
import com.onlinefood.menu_service.dto.MenuUpdateRequestDTO;
import com.onlinefood.menu_service.exception.MenuException;
import com.onlinefood.menu_service.service.MenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/providers/{providerId}/menus")
public class MenuController {

    private final MenuService service;

    @Value("${GATEWAY_INTERNAL_SECRET}")
    private String gatewaySecret;

    public MenuController(MenuService service) {
        this.service = service;
    }

    // ================= PROVIDER =================

    @PostMapping
    public Object createMenu(
            @PathVariable String providerId,
            @RequestHeader(value = "X-INTERNAL-KEY", required = false) String internalKey,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @RequestHeader(value = "X-ROLE", required = false) String role,
            @Valid @RequestBody MenuCreateRequestDTO dto) {

        authorizeProvider(providerId, userId, role, internalKey);
        return service.createMenu(providerId, dto);
    }

    @GetMapping
    public Object getProviderMenus(
            @PathVariable String providerId,
            @RequestHeader("X-INTERNAL-KEY") String internalKey,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role) {

        authorizeProvider(providerId, userId, role, internalKey);
        return service.getMenusByProvider(providerId);
    }

    @PutMapping("/{menuId}")
    public Object updateMenu(
            @PathVariable String providerId,
            @PathVariable String menuId,
            @RequestHeader("X-INTERNAL-KEY") String internalKey,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role,
            @Valid @RequestBody MenuUpdateRequestDTO dto) {

        authorizeProvider(providerId, userId, role, internalKey);
        return service.updateMenu(menuId, providerId, dto);
    }

    @PatchMapping("/{menuId}/status")
    public Object toggleMenu(
            @PathVariable String providerId,
            @PathVariable String menuId,
            @RequestHeader("X-INTERNAL-KEY") String internalKey,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role,
            @RequestParam boolean active) {

        authorizeProvider(providerId, userId, role, internalKey);
        return service.updateMenuStatus(menuId, providerId, active);
    }

    // ================= PUBLIC =================

    @GetMapping("/public")
    public Object getPublicMenus(@PathVariable String providerId) {
        return service.getActiveMenusByProvider(providerId);
    }

    // ================= COMMON =================

    private void authorizeProvider(
            String providerId,
            String userId,
            String role,
            String internalKey
    ) {
        if (internalKey == null || !internalKey.equals(gatewaySecret)) {
            throw new MenuException("Direct access forbidden");
        }
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
