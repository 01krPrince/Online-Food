package com.onlinefood.menu_service.controller;

import com.onlinefood.menu_service.dto.CategoryRequestDTO;
import com.onlinefood.menu_service.dto.MenuItemRequestDTO;
import com.onlinefood.menu_service.service.MenuCatalogService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
public class MenuCatalogController {

    private final MenuCatalogService service;

    public MenuCatalogController(MenuCatalogService service) {
        this.service = service;
    }

    // ADMIN ONLY
    @PostMapping("/categories")
    public Object createCategory(
            @RequestHeader("X-ROLE") String role,
            @Valid @RequestBody CategoryRequestDTO dto) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only ADMIN can create categories");
        }
        return service.createCategory(dto);
    }

    // ADMIN ONLY
    @PostMapping("/items")
    public Object createItem(
            @RequestHeader("X-ROLE") String role,
            @Valid @RequestBody MenuItemRequestDTO dto) {

        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new RuntimeException("Only ADMIN can create menu items");
        }
        return service.createMenuItem(dto);
    }

    // PUBLIC (used by provider & customer)
    @GetMapping("/items")
    public Object getAllItems() {
        return service.getAllItems();
    }
}
