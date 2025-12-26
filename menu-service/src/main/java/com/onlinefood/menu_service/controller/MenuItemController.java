package com.onlinefood.menu_service.controller;

import com.onlinefood.menu_service.dto.MenuItemCreateRequestDTO;
import com.onlinefood.menu_service.dto.MenuItemUpdateRequestDTO;
import com.onlinefood.menu_service.enums.FoodType;
import com.onlinefood.menu_service.enums.MealType;
import com.onlinefood.menu_service.exception.MenuException;
import com.onlinefood.menu_service.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {

    private final MenuItemService service;

    public MenuItemController(MenuItemService service) {
        this.service = service;
    }

    // ================= PROVIDER =================

    @PostMapping("/menu/{menuId}")
    public Object addMenuItem(
            @PathVariable String menuId,
            @RequestHeader(value = "X-USER-ID", required = false) String userId,
            @RequestHeader(value = "X-ROLE", required = false) String role,
            @Valid @RequestBody MenuItemCreateRequestDTO dto) {

        authorizeProvider(userId, role);
        return service.addMenuItem(menuId, userId, dto);
    }

    @PutMapping("/{itemId}")
    public Object updateMenuItem(
            @PathVariable String itemId,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role,
            @Valid @RequestBody MenuItemUpdateRequestDTO dto) {

        authorizeProvider(userId, role);
        return service.updateMenuItem(itemId, userId, dto);
    }

    @PatchMapping("/{itemId}/availability")
    public Object toggleAvailability(
            @PathVariable String itemId,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role,
            @RequestParam boolean available) {

        authorizeProvider(userId, role);
        return service.updateAvailability(itemId, userId, available);
    }

    // ================= PUBLIC =================

    @GetMapping("/public")
    public Object discoverItems(
            @RequestParam(required = false) MealType mealType,
            @RequestParam(required = false) FoodType foodType) {

        return service.discoverItems(mealType, foodType);
    }

    @GetMapping("/public/provider/{providerId}")
    public Object getProviderItems(@PathVariable String providerId) {
        return service.getAvailableItemsByProvider(providerId);
    }

    // ================= COMMON =================

    private void authorizeProvider(String userId, String role) {
        if (userId == null || role == null) {
            throw new MenuException("Unauthorized access (gateway only)");
        }
        if (!"PROVIDER".equalsIgnoreCase(role)) {
            throw new MenuException("Only PROVIDER allowed");
        }
    }
}
