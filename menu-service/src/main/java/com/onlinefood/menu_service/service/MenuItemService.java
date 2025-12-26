package com.onlinefood.menu_service.service;

import com.onlinefood.menu_service.dto.MenuItemCreateRequestDTO;
import com.onlinefood.menu_service.dto.MenuItemUpdateRequestDTO;
import com.onlinefood.menu_service.enums.FoodType;
import com.onlinefood.menu_service.enums.MealType;

public interface MenuItemService {

    Object addMenuItem(String menuId, String providerId, MenuItemCreateRequestDTO dto);

    Object updateMenuItem(String itemId, String providerId, MenuItemUpdateRequestDTO dto);

    Object updateAvailability(String itemId, String providerId, boolean available);

    Object discoverItems(MealType mealType, FoodType foodType);

    Object getAvailableItemsByProvider(String providerId);
}
