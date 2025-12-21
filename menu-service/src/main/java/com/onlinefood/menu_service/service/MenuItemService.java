package com.onlinefood.menu_service.service;

import com.onlinefood.menu_service.dto.MenuItemCreateRequestDTO;
import com.onlinefood.menu_service.dto.MenuItemUpdateRequestDTO;
import com.onlinefood.menu_service.enums.FoodType;
import com.onlinefood.menu_service.enums.MealType;
import com.onlinefood.menu_service.model.MenuItem;

import java.util.List;

public interface MenuItemService {

    MenuItem addMenuItem(String menuId, String providerId, MenuItemCreateRequestDTO dto);

    MenuItem updateAvailability(String itemId, String providerId, boolean available);

    List<MenuItem> discoverItems(MealType mealType, FoodType foodType);

    List<MenuItem> getAvailableItemsByProvider(String providerId);

    MenuItem updateMenuItem(
            String itemId,
            String providerId,
            MenuItemUpdateRequestDTO dto);

}
