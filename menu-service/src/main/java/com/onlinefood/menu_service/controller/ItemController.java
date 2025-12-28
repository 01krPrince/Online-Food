package com.onlinefood.menu_service.controller;

import com.onlinefood.menu_service.dto.BulkMenuItemUpdateDTO;
import com.onlinefood.menu_service.dto.ItemCreateRequestDTO;
import com.onlinefood.menu_service.dto.ItemUpdateRequestDTO;
import com.onlinefood.menu_service.exception.MenuException;
import com.onlinefood.menu_service.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    @Value("${GATEWAY_INTERNAL_SECRET}")
    private String gatewaySecret;

    public ItemController(ItemService service) {
        this.service = service;
    }

    // ================= CREATE ITEM =================

    @PostMapping
    public Object createItem(
            @RequestHeader("X-INTERNAL-KEY") String internalKey,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role,
            @Valid @RequestBody ItemCreateRequestDTO dto) {

        authorizeProvider(userId, role, internalKey);
        return service.createItem(userId, dto);
    }

    // ================= UPDATE ITEM =================

    @PutMapping("/{itemId}")
    public Object updateItem(
            @PathVariable String itemId,
            @RequestHeader("X-INTERNAL-KEY") String internalKey,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role,
            @Valid @RequestBody ItemUpdateRequestDTO dto) {

        authorizeProvider(userId, role, internalKey);
        return service.updateItem(itemId, userId, dto);
    }

    @PatchMapping("/bulk-menu-update/{menuId}")
    public Object bulkUpdateMenuItems(
            @PathVariable String menuId,
            @RequestHeader("X-INTERNAL-KEY") String internalKey,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role,
            @Valid @RequestBody BulkMenuItemUpdateDTO dto) {

        authorizeProvider(userId, role, internalKey);
        return service.bulkUpdateMenuItems(menuId, userId, dto);
    }


    // ================= MENU ASSIGN =================

    @PatchMapping("/{itemId}/assign-menu/{menuId}")
    public Object assignMenu(
            @PathVariable String itemId,
            @PathVariable String menuId,
            @RequestHeader("X-INTERNAL-KEY") String internalKey,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role) {

        authorizeProvider(userId, role, internalKey);
        return service.assignMenu(itemId, menuId, userId);
    }

    @PatchMapping("/{itemId}/remove-menu/{menuId}")
    public Object removeMenu(
            @PathVariable String itemId,
            @PathVariable String menuId,
            @RequestHeader("X-INTERNAL-KEY") String internalKey,
            @RequestHeader("X-USER-ID") String userId,
            @RequestHeader("X-ROLE") String role) {

        authorizeProvider(userId, role, internalKey);
        return service.removeMenu(itemId, menuId, userId);
    }

    // ================= PUBLIC =================

    @GetMapping("/public/menu/{menuId}")
    public Object getItemsByMenu(@PathVariable String menuId) {
        return service.getItemsByMenu(menuId);
    }

    // ================= COMMON =================

    private void authorizeProvider(String userId, String role, String internalKey) {
        if (!gatewaySecret.equals(internalKey)) {
            throw new MenuException("Direct access forbidden");
        }
        if (!"PROVIDER".equalsIgnoreCase(role)) {
            throw new MenuException("Only PROVIDER allowed");
        }
    }
}
