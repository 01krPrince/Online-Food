package com.onlinefood.menu_service.service.serviceImpl;

import com.onlinefood.menu_service.dto.*;
import com.onlinefood.menu_service.exception.MenuException;
import com.onlinefood.menu_service.model.Item;
import com.onlinefood.menu_service.repository.ItemRepository;
import com.onlinefood.menu_service.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;

    @Autowired
    public ItemServiceImpl(ItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public Object createItem(String providerId, ItemCreateRequestDTO dto) {

        Item item = new Item();
        item.setProviderId(providerId);
        item.setMenuIds(null);

        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        item.setImageUrl(dto.getImageUrl());

        item.setFoodType(dto.getFoodType());
        item.setMealType(dto.getMealType());
        item.setOrderType(dto.getOrderType());

        item.setIncludedInTiffin(Boolean.TRUE.equals(dto.getIncludedInTiffin()));
        item.setOptionalAddon(Boolean.TRUE.equals(dto.getOptionalAddon()));
        item.setAvailable(dto.getAvailable() != null ? dto.getAvailable() : true);

        item.setTags(dto.getTags());
        item.setRating(0);
        item.setRatingCount(0);

        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());

        return repository.save(item);
    }

    @Override
    public Object updateItem(String itemId, String providerId, ItemUpdateRequestDTO dto) {

        Item item = getOwnedItem(itemId, providerId);

        if (dto.getName() != null) item.setName(dto.getName());
        if (dto.getDescription() != null) item.setDescription(dto.getDescription());
        if (dto.getPrice() != null) item.setPrice(dto.getPrice());
        if (dto.getImageUrl() != null) item.setImageUrl(dto.getImageUrl());
        if (dto.getFoodType() != null) item.setFoodType(dto.getFoodType());
        if (dto.getMealType() != null) item.setMealType(dto.getMealType());
        if (dto.getOrderType() != null) item.setOrderType(dto.getOrderType());
        if (dto.getIncludedInTiffin() != null) item.setIncludedInTiffin(dto.getIncludedInTiffin());
        if (dto.getOptionalAddon() != null) item.setOptionalAddon(dto.getOptionalAddon());
        if (dto.getAvailable() != null) item.setAvailable(dto.getAvailable());
        if (dto.getTags() != null) item.setTags(dto.getTags());

        item.setUpdatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    @Override
    public Object assignMenu(String itemId, String menuId, String providerId) {

        Item item = getOwnedItem(itemId, providerId);

        if (!item.getMenuIds().contains(menuId)) {
            item.getMenuIds().add(menuId);
        }

        item.setUpdatedAt(LocalDateTime.now());
        return repository.save(item);
    }

    @Override
    public Object removeMenu(String itemId, String menuId, String providerId) {

        Item item = getOwnedItem(itemId, providerId);

        item.getMenuIds().remove(menuId);
        item.setUpdatedAt(LocalDateTime.now());

        return repository.save(item);
    }

    @Override
    public Object getItemsByMenu(String menuId) {
        return repository.findByMenuIdsContaining(menuId);
    }

    // 🔥 BULK MENU UPDATE (IMPORTANT)

    @Override
    public Object bulkUpdateMenuItems(
            String menuId,
            String providerId,
            BulkMenuItemUpdateDTO dto) {

        if (dto.getAddItemIds() != null) {
            List<Item> addItems =
                    repository.findByIdInAndProviderId(
                            dto.getAddItemIds(), providerId
                    );

            for (Item item : addItems) {
                if (!item.getMenuIds().contains(menuId)) {
                    item.getMenuIds().add(menuId);
                    item.setUpdatedAt(LocalDateTime.now());
                }
            }
            repository.saveAll(addItems);
        }

        if (dto.getRemoveItemIds() != null) {
            List<Item> removeItems =
                    repository.findByIdInAndProviderId(
                            dto.getRemoveItemIds(), providerId
                    );

            for (Item item : removeItems) {
                item.getMenuIds().remove(menuId);
                item.setUpdatedAt(LocalDateTime.now());
            }
            repository.saveAll(removeItems);
        }

        return "Menu items updated successfully";
    }

    // 🔒 OWNERSHIP CHECK
    private Item getOwnedItem(String itemId, String providerId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new MenuException("Item not found"));

        if (!item.getProviderId().equals(providerId)) {
            throw new MenuException("Unauthorized item access");
        }
        return item;
    }
}
