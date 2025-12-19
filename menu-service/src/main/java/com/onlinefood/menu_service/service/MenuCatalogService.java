package com.onlinefood.menu_service.service;

import com.onlinefood.menu_service.dto.CategoryRequestDTO;
import com.onlinefood.menu_service.dto.MenuItemRequestDTO;
import com.onlinefood.menu_service.model.Category;
import com.onlinefood.menu_service.model.MenuItem;

import java.util.List;

public interface MenuCatalogService {

    Category createCategory(CategoryRequestDTO dto);

    MenuItem createMenuItem(MenuItemRequestDTO dto);

    List<MenuItem> getAllItems();
}
