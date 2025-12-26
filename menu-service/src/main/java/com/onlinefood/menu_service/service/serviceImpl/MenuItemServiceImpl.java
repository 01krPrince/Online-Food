package com.onlinefood.menu_service.service.serviceImpl;

import com.onlinefood.menu_service.dto.MenuItemCreateRequestDTO;
import com.onlinefood.menu_service.dto.MenuItemUpdateRequestDTO;
import com.onlinefood.menu_service.enums.FoodType;
import com.onlinefood.menu_service.enums.MealType;
import com.onlinefood.menu_service.exception.ResourceNotFoundException;
import com.onlinefood.menu_service.exception.UnauthorizedException;
import com.onlinefood.menu_service.model.MenuItem;
import com.onlinefood.menu_service.repository.MenuItemRepository;
import com.onlinefood.menu_service.service.MenuItemService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository repository;

    public MenuItemServiceImpl(MenuItemRepository repository) {
        this.repository = repository;
    }

    // ================= CREATE =================

    @Override
    public Object addMenuItem(
            String menuId,
            String providerId,
            MenuItemCreateRequestDTO dto) {

        MenuItem item = new MenuItem();
        item.setMenuId(menuId);
        item.setProviderId(providerId);

        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setImageUrl(dto.getImageUrl());

        item.setFoodType(dto.getFoodType());
        item.setMealType(dto.getMealType());
        item.setOrderType(dto.getOrderType());

        item.setIncludedInTiffin(Boolean.TRUE.equals(dto.getIncludedInTiffin()));
        item.setOptionalAddon(Boolean.TRUE.equals(dto.getOptionalAddon()));
        item.setAvailable(dto.isAvailable());

        item.setTags(dto.getTags());

        item.setRating(0);
        item.setRatingCount(0);

        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());

        return repository.save(item);
    }

    // ================= UPDATE =================

    @Override
    public Object updateMenuItem(
            String itemId,
            String providerId,
            MenuItemUpdateRequestDTO dto) {

        MenuItem item = repository.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Menu item not found"));

        if (!item.getProviderId().equals(providerId)) {
            throw new UnauthorizedException("Unauthorized menu item access");
        }

        if (dto.getName() != null) item.setName(dto.getName());
        if (dto.getDescription() != null) item.setDescription(dto.getDescription());
        if (dto.getPrice() != null) item.setPrice(dto.getPrice());
        if (dto.getImageUrl() != null) item.setImageUrl(dto.getImageUrl());
        if (dto.getFoodType() != null) item.setFoodType(dto.getFoodType());
        if (dto.getMealType() != null) item.setMealType(dto.getMealType());
        if (dto.getOrderType() != null) item.setOrderType(dto.getOrderType());
        if (dto.getIncludedInTiffin() != null)
            item.setIncludedInTiffin(dto.getIncludedInTiffin());
        if (dto.getOptionalAddon() != null)
            item.setOptionalAddon(dto.getOptionalAddon());
        if (dto.getAvailable() != null)
            item.setAvailable(dto.getAvailable());
        if (dto.getTags() != null)
            item.setTags(dto.getTags());

        item.setUpdatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    // ================= AVAILABILITY =================

    @Override
    public Object updateAvailability(
            String itemId,
            String providerId,
            boolean available) {

        MenuItem item = repository.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Menu item not found"));

        if (!item.getProviderId().equals(providerId)) {
            throw new UnauthorizedException("Unauthorized menu item access");
        }

        item.setAvailable(available);
        item.setUpdatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    // ================= PUBLIC =================

    @Override
    public Object discoverItems(MealType mealType, FoodType foodType) {

        if (mealType != null && foodType != null) {
            return repository.findByAvailableTrueAndMealTypeAndFoodType(
                    mealType, foodType
            );
        }

        if (mealType != null) {
            return repository.findByAvailableTrueAndMealType(mealType);
        }

        if (foodType != null) {
            return repository.findByAvailableTrueAndFoodType(foodType);
        }

        return repository.findByAvailableTrue();
    }

    @Override
    public Object getAvailableItemsByProvider(String providerId) {
        return repository.findByProviderIdAndAvailableTrue(providerId);
    }
}
