package com.onlinefood.menu_service.service.serviceImpl;

import com.onlinefood.menu_service.dto.MenuItemCreateRequestDTO;
import com.onlinefood.menu_service.dto.MenuItemUpdateRequestDTO;
import com.onlinefood.menu_service.enums.FoodType;
import com.onlinefood.menu_service.enums.MealType;
import com.onlinefood.menu_service.exception.MenuException;
import com.onlinefood.menu_service.model.Menu;
import com.onlinefood.menu_service.model.MenuItem;
import com.onlinefood.menu_service.repository.MenuItemRepository;
import com.onlinefood.menu_service.repository.MenuRepository;
import com.onlinefood.menu_service.service.MenuItemService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository itemRepository;
    private final MenuRepository menuRepository;

    public MenuItemServiceImpl(
            MenuItemRepository itemRepository,
            MenuRepository menuRepository) {

        this.itemRepository = itemRepository;
        this.menuRepository = menuRepository;
    }

    // ================= ADD MENU ITEM =================

    @Override
    public MenuItem addMenuItem(
            String menuId,
            String providerId,
            MenuItemCreateRequestDTO dto) {

        // 1️⃣ Validate menu existence
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuException("Menu not found"));

        // 2️⃣ Ownership check
        if (!menu.getProviderId().equals(providerId)) {
            throw new MenuException("Unauthorized menu access");
        }

        // 3️⃣ Map DTO → Entity
        MenuItem item = new MenuItem();
        item.setMenuId(menuId);
        item.setProviderId(providerId);

        item.setName(dto.getName().trim());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setImageUrl(dto.getImageUrl());
        item.setFoodType(dto.getFoodType());
        item.setMealType(dto.getMealType());
        item.setTags(dto.getTags());

        // 4️⃣ Defaults
        item.setAvailable(true);
        item.setRating(0.0);
        item.setRatingCount(0);

        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());

        return itemRepository.save(item);
    }

    // ================= UPDATE AVAILABILITY =================

    @Override
    public MenuItem updateAvailability(
            String itemId,
            String providerId,
            boolean available) {

        MenuItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new MenuException("Menu item not found"));

        if (!item.getProviderId().equals(providerId)) {
            throw new MenuException("Unauthorized item access");
        }

        item.setAvailable(available);
        item.setUpdatedAt(LocalDateTime.now());

        return itemRepository.save(item);
    }

    // ================= DISCOVER ITEMS =================

    @Override
    public List<MenuItem> discoverItems(
            MealType mealType,
            FoodType foodType) {

        if (mealType != null && foodType != null) {
            return itemRepository
                    .findByMealTypeAndFoodTypeAndAvailableTrue(mealType, foodType);
        }

        if (mealType != null) {
            return itemRepository.findByMealTypeAndAvailableTrue(mealType);
        }

        if (foodType != null) {
            return itemRepository.findByFoodTypeAndAvailableTrue(foodType);
        }

        throw new MenuException("At least one filter is required");
    }

    // ================= PROVIDER ITEMS =================

    @Override
    public List<MenuItem> getAvailableItemsByProvider(String providerId) {
        return itemRepository.findByProviderIdAndAvailableTrue(providerId);
    }

    @Override
    public MenuItem updateMenuItem(
            String itemId,
            String providerId,
            MenuItemUpdateRequestDTO dto) {

        MenuItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new MenuException("Menu item not found"));

        if (!item.getProviderId().equals(providerId)) {
            throw new MenuException("Unauthorized item access");
        }

        if (dto.getName() != null) {
            item.setName(dto.getName().trim());
        }

        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }

        if (dto.getPrice() != null) {
            item.setPrice(dto.getPrice());
        }

        if (dto.getImageUrl() != null) {
            item.setImageUrl(dto.getImageUrl());
        }

        if (dto.getTags() != null) {
            item.setTags(dto.getTags());
        }

        item.setUpdatedAt(LocalDateTime.now());

        return itemRepository.save(item);
    }

}
