package com.onlinefood.menu_service.service;

import com.onlinefood.menu_service.dto.*;

public interface ItemService {

    Object createItem(String providerId, ItemCreateRequestDTO dto);

    Object updateItem(String itemId, String providerId, ItemUpdateRequestDTO dto);

    Object assignMenu(String itemId, String menuId, String providerId);

    Object removeMenu(String itemId, String menuId, String providerId);

    Object getItemsByMenu(String menuId);

    Object bulkUpdateMenuItems(
            String menuId,
            String providerId,
            BulkMenuItemUpdateDTO dto
    );
}
