package com.onlinefood.menu_service.service;

import com.onlinefood.menu_service.dto.MenuCreateRequestDTO;
import com.onlinefood.menu_service.dto.MenuUpdateRequestDTO;

public interface MenuService {

    Object createMenu(String providerId, MenuCreateRequestDTO dto);

    Object getMenusByProvider(String providerId);

    Object getActiveMenusByProvider(String providerId);

    Object updateMenu(String menuId, String providerId, MenuUpdateRequestDTO dto);

    Object updateMenuStatus(String menuId, String providerId, boolean active);
}
